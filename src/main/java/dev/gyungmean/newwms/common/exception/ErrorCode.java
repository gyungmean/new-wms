package dev.gyungmean.newwms.common.exception;

public final class ErrorCode {

    private ErrorCode() {}

    // Rack
    public static final String RACK_NOT_AVAILABLE     = "rack.state.not_available";
    public static final String RACK_NOT_INGRESS       = "rack.state.not_ingress";
    public static final String RACK_NOT_OUTBOUND      = "rack.state.not_outbound";
    public static final String RACK_NOT_FOUND         = "rack.not_found";
    public static final String RACK_DUPLICATE         = "rack.duplicate";
    public static final String RACK_NO_PARTNER        = "rack.no_partner_configured";
    public static final String RACK_PARTNER_NOT_FOUND = "rack.partner_not_found";

    // RackAddress
    public static final String RACK_ADDRESS_NULL      = "rack.address.null";
    public static final String RACK_ADDRESS_INVALID_S = "rack.address.invalid_s";
    public static final String RACK_ADDRESS_INVALID_Z = "rack.address.invalid_z";
    public static final String RACK_ADDRESS_INVALID_X = "rack.address.invalid_x";
    public static final String RACK_ADDRESS_INVALID_Y = "rack.address.invalid_y";
    public static final String RACK_ADDRESS_NO_SIDE   = "rack.address.no_side_rack";

    // Item
    public static final String ITEM_NOT_FOUND         = "item.not_found";
    public static final String ITEM_DUPLICATE         = "item.duplicate";

    // Storage
    public static final String STORAGE_NOT_FOUND      = "storage.not_found";
    public static final String STORAGE_DUPLICATE      = "storage.duplicate";

    // Stock
    public static final String STOCK_QUANTITY_POSITIVE = "stock.quantity.must_be_positive";
    public static final String STOCK_IS_RESERVED = "stock.state.reserved";
    public static final String STOCK_IS_NOT_RESERVED = "stock.state.not_reserved";
    public static final String STOCK_IS_HOLD = "stock.state.hold";
    public static final String STOCK_IS_NOT_HOLD = "stock.state.not_hold";
    public static final String STOCK_CANNOT_MERGED = "stock.can.not_merged";

    // Common
    public static final String INTERNAL_ERROR         = "error.internal";
}
