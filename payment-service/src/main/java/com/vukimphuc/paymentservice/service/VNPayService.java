package com.vukimphuc.paymentservice.service;

import com.vukimphuc.paymentservice.helpers.VNPayHepler;
import com.vukimphuc.paymentservice.constants.VNPayConstant;
import com.vukimphuc.paymentservice.dto.request.OrderRequestDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class VNPayService {
    public Map<String, Object> createOrder(HttpServletRequest request, OrderRequestDTO orderRequestDTO) throws UnsupportedEncodingException {

        Map<String, Object> payload = new HashMap(){{
            put("vnp_Version", VNPayConstant.VNP_VERSION);
            put("vnp_Command", VNPayConstant.VNP_COMMAND_ORDER);
            put("vnp_TmnCode", VNPayConstant.VNP_TMN_CODE);
            put("vnp_Amount", String.valueOf(orderRequestDTO.getAmount() * 100));
            //put("vnp_BankCode", VNPayConstant.VNP_BANK_CODE);
            put("vnp_CurrCode", VNPayConstant.VNP_CURRENCY_CODE);
            put("vnp_TxnRef",  VNPayHepler.getRandomNumber(8));
            put("vnp_OrderInfo", orderRequestDTO.getOrderInfor());
            put("vnp_OrderType", VNPayConstant.ORDER_TYPE);
            put("vnp_Locale", VNPayConstant.VNP_LOCALE);
            put("vnp_ReturnUrl", VNPayConstant.VNP_RETURN_URL);
            put("vnp_IpAddr", VNPayHepler.getIpAddress(request));
            put("vnp_CreateDate", VNPayHepler.generateDate(false));
            put("vnp_ExpireDate", VNPayHepler.generateDate(true));
        }};

        String queryUrl = getQueryUrl(payload).get("queryUrl")
                + "&vnp_SecureHash="
                + VNPayHepler.hmacSHA512(VNPayConstant.SECRET_KEY, getQueryUrl(payload).get("hashData"));

        String paymentUrl = VNPayConstant.VNP_PAY_URL + "?" + queryUrl;
        payload.put("redirect_url", paymentUrl);

        return payload;
    }

    private Map<String, String> getQueryUrl(Map<String, Object> payload) throws UnsupportedEncodingException {

        List<String> fieldNames = new ArrayList<>(payload.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {

            String fieldName = (String) itr.next();
            String fieldValue = (String) payload.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {

                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {

                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        Map<String, String> result = new HashMap<>();
        result. put("queryUrl", query.toString());
        result. put("hashData", hashData.toString());
        return result;
    }

}
