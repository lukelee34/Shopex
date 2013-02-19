package net.shopxx.action.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.jsp.PageContext;

import net.shopxx.bean.SystemConfig.PointType;
import net.shopxx.bean.SystemConfig.StoreFreezeTime;
import net.shopxx.entity.CartItem;
import net.shopxx.entity.DeliveryType;
import net.shopxx.entity.Order;
import net.shopxx.entity.OrderItem;
import net.shopxx.entity.OrderLog;
import net.shopxx.entity.PaymentConfig;
import net.shopxx.entity.Product;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.DeliveryType.DeliveryMethod;
import net.shopxx.entity.Order.OrderStatus;
import net.shopxx.entity.Order.PaymentStatus;
import net.shopxx.entity.Order.ShippingStatus;
import net.shopxx.entity.OrderLog.OrderLogType;
import net.shopxx.entity.Product.WeightUnit;
import net.shopxx.service.AreaService;
import net.shopxx.service.CartItemService;
import net.shopxx.service.DeliveryTypeService;
import net.shopxx.service.HtmlService;
import net.shopxx.service.OrderItemService;
import net.shopxx.service.OrderLogService;
import net.shopxx.service.OrderService;
import net.shopxx.service.PaymentConfigService;
import net.shopxx.service.ProductService;
import net.shopxx.service.ReceiverService;
import net.shopxx.util.ArithUtil;
import net.shopxx.util.SystemConfigUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Hibernate;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 前台Action类 - 订单处理
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXX66590FE3DF97C81824D62C168A48301D
 * ============================================================================
 */

@ParentPackage("member")
@InterceptorRefs({
	@InterceptorRef(value = "token", params = {"excludeMethods", "info,list,view"}),
	@InterceptorRef(value = "memberStack")
})
public class OrderAction extends BaseShopAction {
	
	private static final long serialVersionUID = 2553137844831167917L;
	
	private Boolean isSaveReceiver;// 是否保存收货地址
	private Integer totalQuantity;// 商品总数
	private Integer totalPoint;// 总积分
	private Double totalWeightGram;// 商品总重量（单位：g）
	private BigDecimal productTotalPrice;// 总计商品价格
	private String memo;// 附言
	
	private Receiver receiver;// 其它收货地址
	private DeliveryType deliveryType;// 配送方式
	private PaymentConfig paymentConfig;// 支付方式
	private Order order;// 订单
	private Set<CartItem> cartItemSet;// 购物车项

	@Resource
	private ReceiverService receiverService;
	@Resource
	private AreaService areaService;
	@Resource
	private DeliveryTypeService deliveryTypeService;
	@Resource
	private PaymentConfigService paymentConfigService;
	@Resource
	private OrderService orderService;
	@Resource
	private OrderLogService orderLogService;
	@Resource
	private CartItemService cartItemService;
	@Resource
	private OrderItemService orderItemService;
	@Resource
	private ProductService productService;
	@Resource
	private HtmlService htmlService;
	
	// 订单信息
	@InputConfig(resultName = "error")
	public String info() {
		cartItemSet = getLoginMember().getCartItemSet();
		if (cartItemSet == null || cartItemSet.size() == 0) {
			addActionError("购物车目前没有加入任何商品！");
			return ERROR;
		}
		for (CartItem cartItem : cartItemSet) {
			Product product = cartItem.getProduct();
			if (product.getStore() != null && (cartItem.getQuantity() + product.getFreezeStore()) > product.getStore()) {
				addActionError("商品库存不足，请返回修改！");
				return ERROR;
			}
		}
		totalQuantity = 0;
		totalPoint = 0;
		totalWeightGram = 0D;
		productTotalPrice = new BigDecimal("0");
		for (CartItem cartItem : cartItemSet) {
			totalQuantity += cartItem.getQuantity();
			if (getSystemConfig().getPointType() == PointType.productSet) {
				totalPoint = cartItem.getProduct().getPoint() * cartItem.getQuantity() + totalPoint;
			}
			productTotalPrice = cartItem.getProduct().getPreferentialPrice(getLoginMember()).multiply(new BigDecimal(cartItem.getQuantity().toString())).add(productTotalPrice);
			Product product = cartItem.getProduct();
			Double weightGram = DeliveryType.toWeightGram(product.getWeight(), product.getWeightUnit());
			totalWeightGram = ArithUtil.add(totalWeightGram, ArithUtil.mul(weightGram, cartItem.getQuantity()));
		}
		productTotalPrice = SystemConfigUtil.getOrderScaleBigDecimal(productTotalPrice);
		if (getSystemConfig().getPointType() == PointType.orderAmount) {
			totalPoint = productTotalPrice.multiply(new BigDecimal(getSystemConfig().getPointScale().toString())).setScale(0, RoundingMode.DOWN).intValue();
		}
		setResponseNoCache();
		return "info";
	}
	
	// 订单保存
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "deliveryType.id", message = "请选择配送方式！")
		}
	)
	@InputConfig(resultName = "error")
	public String save() {
		cartItemSet = getLoginMember().getCartItemSet();
		if (cartItemSet == null || cartItemSet.size() == 0) {
			addActionError("购物车目前没有加入任何商品！");
			return ERROR;
		}
		if (StringUtils.isNotEmpty(receiver.getId())) {
			receiver = receiverService.load(receiver.getId());
			if (areaService.getAreaString(receiver.getAreaPath()) == null) {
				addActionError("收货地址信息不完整，请补充收货地址信息！");
				redirectionUrl = "receiver!edit.action?id=" + receiver.getId();
				return ERROR;
			}
		} else {
			if (StringUtils.isEmpty(receiver.getName())) {
				addActionError("收货人不允许为空！");
				return ERROR;
			}
			if (StringUtils.isEmpty(receiver.getAreaPath())) {
				addActionError("地区不允许为空！");
				return ERROR;
			}
			if (StringUtils.isEmpty(receiver.getAddress())) {
				addActionError("联系地址不允许为空！");
				return ERROR;
			}
			if (StringUtils.isEmpty(receiver.getZipCode())) {
				addActionError("邮编不允许为空！");
				return ERROR;
			}
			if (StringUtils.isEmpty(receiver.getPhone()) && StringUtils.isEmpty(receiver.getMobile())) {
				addActionError("联系电话、联系手机必须填写其中一项！");
				return ERROR;
			}
			if (!areaService.isAreaPath(receiver.getAreaPath())) {
				addActionError("地区错误！");
				return ERROR;
			}
			if (isSaveReceiver == null) {
				addActionError("是否保存不允许为空！");
				return ERROR;
			}
			if (isSaveReceiver) {
				receiver.setIsDefault(false);
				receiver.setMember(getLoginMember());
				receiverService.save(receiver);
			}
		}
		for (CartItem cartItem : cartItemSet) {
			Product product = cartItem.getProduct();
			if (product.getStore() != null && (cartItem.getQuantity() + product.getFreezeStore() > product.getStore())) {
				addActionError("商品[" + product.getName() + "]库存不足！");
				return ERROR;
			}
		}
		deliveryType = deliveryTypeService.load(deliveryType.getId());
		if (deliveryType.getDeliveryMethod() == DeliveryMethod.deliveryAgainstPayment && (paymentConfig == null && StringUtils.isEmpty(paymentConfig.getId()))) {
			addActionError("请选择支付方式！");
			return ERROR;
		}
		totalQuantity = 0;
		productTotalPrice = new BigDecimal("0");
		totalWeightGram = 0D;
		for (CartItem cartItem : cartItemSet) {
			Product product = cartItem.getProduct();
			totalQuantity += cartItem.getQuantity();
			productTotalPrice = cartItem.getProduct().getPreferentialPrice(getLoginMember()).multiply(new BigDecimal(cartItem.getQuantity().toString())).add(productTotalPrice);
			Double weightGram = DeliveryType.toWeightGram(product.getWeight(), product.getWeightUnit());
			totalWeightGram = ArithUtil.add(totalWeightGram, ArithUtil.mul(weightGram, cartItem.getQuantity()));
			cartItemService.delete(cartItem);
		}
		productTotalPrice = SystemConfigUtil.getOrderScaleBigDecimal(productTotalPrice);
		BigDecimal deliveryFee = deliveryType.getDeliveryFee(totalWeightGram);
		
		String paymentConfigName = null;
		BigDecimal paymentFee = null;
		if (deliveryType.getDeliveryMethod() == DeliveryMethod.deliveryAgainstPayment) {
			paymentConfig = paymentConfigService.load(paymentConfig.getId());
			paymentConfigName = paymentConfig.getName();
			paymentFee = paymentConfig.getPaymentFee(productTotalPrice.add(deliveryFee));
		} else {
			paymentConfig = null;
			paymentConfigName = "货到付款";
			paymentFee = new BigDecimal("0");
		}
		
		BigDecimal totalAmount = productTotalPrice.add(deliveryFee).add(paymentFee);
		
		order = new Order();
		order.setOrderStatus(OrderStatus.unprocessed);
		order.setPaymentStatus(PaymentStatus.unpaid);
		order.setShippingStatus(ShippingStatus.unshipped);
		order.setDeliveryTypeName(deliveryType.getName());
		order.setPaymentConfigName(paymentConfigName);
		order.setProductTotalPrice(productTotalPrice);
		order.setDeliveryFee(deliveryFee);
		order.setPaymentFee(paymentFee);
		order.setTotalAmount(totalAmount);
		order.setPaidAmount(new BigDecimal("0"));
		if (totalWeightGram < 1000) {
			order.setProductWeight(totalWeightGram);
			order.setProductWeightUnit(WeightUnit.g);
		} else if(totalWeightGram >= 1000 && totalWeightGram < 1000000) {
			order.setProductWeight(totalWeightGram / 1000);
			order.setProductWeightUnit(WeightUnit.kg);
		} else if(totalWeightGram >= 1000000) {
			order.setProductWeight(totalWeightGram / 1000000);
			order.setProductWeightUnit(WeightUnit.t);
		}
		order.setProductTotalQuantity(totalQuantity);
		order.setShipName(receiver.getName());
		order.setShipArea(areaService.getAreaString(receiver.getAreaPath()));
		order.setShipAreaPath(receiver.getAreaPath());
		order.setShipAddress(receiver.getAddress());
		order.setShipZipCode(receiver.getZipCode());
		order.setShipPhone(receiver.getPhone());
		order.setShipMobile(receiver.getMobile());
		order.setMemo(memo);
		order.setMember(getLoginMember());
		order.setDeliveryType(deliveryType);
		order.setPaymentConfig(paymentConfig);
		orderService.save(order);
		
		// 商品项
		for (CartItem cartItem : cartItemSet) {
			Product product = cartItem.getProduct();
			OrderItem orderItem = new OrderItem();
			orderItem.setProductSn(product.getProductSn());
			orderItem.setProductName(product.getName());
			orderItem.setProductPrice(product.getPreferentialPrice(getLoginMember()));
			orderItem.setProductQuantity(cartItem.getQuantity());
			orderItem.setDeliveryQuantity(0);
			orderItem.setTotalDeliveryQuantity(0);
			orderItem.setProductHtmlFilePath(product.getHtmlFilePath());
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItemService.save(orderItem);
		}
		
		// 库存处理
		if (getSystemConfig().getStoreFreezeTime() == StoreFreezeTime.order) {
			for (CartItem cartItem : cartItemSet) {
				Product product = cartItem.getProduct();
				if (product.getStore() != null) {
					product.setFreezeStore(product.getFreezeStore() + cartItem.getQuantity());
					if (product.getIsOutOfStock()) {
						Hibernate.initialize(cartItem.getProduct().getProductAttributeMapStore());
					}
					productService.update(product);
					if (product.getIsOutOfStock()) {
						flushCache();
						htmlService.productContentBuildHtml(product);
					}
				}
			}
		}
		
		// 订单日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderLogType(OrderLogType.create);
		orderLog.setOrderSn(order.getOrderSn());
		orderLog.setOperator(null);
		orderLog.setInfo(null);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);
		
		setResponseNoCache();
		return "result";
	}
	
	// 订单列表
	public String list() {
		pager = orderService.getOrderPager(getLoginMember(), pager);
		return "list";
	}
	
	// 订单详情
	public String view() {
		order = orderService.load(id);
		totalPoint = 0;
		if (getSystemConfig().getPointType() == PointType.productSet) {
			for (OrderItem orderItem : order.getOrderItemSet()) {
				totalPoint = orderItem.getProduct().getPoint() * orderItem.getProductQuantity() + totalPoint;
			}
		} else if (getSystemConfig().getPointType() == PointType.orderAmount) {
			totalPoint = order.getProductTotalPrice().multiply(new BigDecimal(getSystemConfig().getPointScale().toString())).setScale(0, RoundingMode.DOWN).intValue();
		}
		return "view";
	}
	
	// 更新页面缓存
	private void flushCache() {
		Cache cache = ServletCacheAdministrator.getInstance(getRequest().getSession().getServletContext()).getCache(getRequest(), PageContext.APPLICATION_SCOPE); 
		cache.flushAll(new Date());
	}
	
	// 获取所有配送方式
	public List<DeliveryType> getAllDeliveryType() {
		return deliveryTypeService.getAll();
	}
	
	// 获取所有支付方式
	public List<PaymentConfig> getAllPaymentConfig() {
		return paymentConfigService.getAll();
	}

	public Boolean getIsSaveReceiver() {
		return isSaveReceiver;
	}

	public void setIsSaveReceiver(Boolean isSaveReceiver) {
		this.isSaveReceiver = isSaveReceiver;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Double getTotalWeightGram() {
		return totalWeightGram;
	}

	public void setTotalWeightGram(Double totalWeightGram) {
		this.totalWeightGram = totalWeightGram;
	}

	public BigDecimal getProductTotalPrice() {
		return productTotalPrice;
	}

	public void setProductTotalPrice(BigDecimal productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public PaymentConfig getPaymentConfig() {
		return paymentConfig;
	}

	public void setPaymentConfig(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Set<CartItem> getCartItemSet() {
		return cartItemSet;
	}

	public void setCartItemSet(Set<CartItem> cartItemSet) {
		this.cartItemSet = cartItemSet;
	}

}