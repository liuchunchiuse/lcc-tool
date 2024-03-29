package com.lcc.tool.utils.request;

import com.google.common.collect.Maps;
import com.lcc.tool.constants.LccConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 请求参数对象
 * @author Liu Chunchi
 * @date 2023/8/29 13:59:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationArgs {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求和响应时间分别是10分钟
     */
    @Builder.Default
    private int timeout = 60 * 10000;

    /**
     * 连接超时时间(毫秒),默认5分钟
     */
    @Builder.Default
    private int connectionTimeout = 60 * 10000;

    /**
     * 读取超时时间(毫秒),默认5分钟
     */
    @Builder.Default
    private int readTimeout = 60 * 10000;

    /**
     * Map类型请求参数
     */
    @Builder.Default
    private Map<String, Object> params = Collections.emptyMap();

    /**
     * json字符串类型请求参数
     */
    private String body;

    /**
     * 请求方式,默认为post
     */
    @Builder.Default
    private Operation.Method method = Operation.Method.POST_BODY;

    /**
     * 请求header,默认为json
     */
    @Builder.Default
    private Operation.Application application = Operation.Application.JSON;

    /**
     * 返回默认结果code字段
     */
    @Builder.Default
    private String returnCodeField = "code";
    /**
     * 返回默认结果data字段
     */
    @Builder.Default
    private String returnDateField = "data";
    /**
     * 返回的消息字段
     */
    @Builder.Default
    private String returnMessageField = "message";

    /**
     * 第三方返回的状态码,默认200
     */
    @Builder.Default
    private Integer returnSuccessCode = LccConstants.SuccessEnum.SUCCESS_2_HUNDRED.getCode();

    /**
     * 业务需要返回的状态码
     */
    @Builder.Default
    private Integer bizReturnSuccessCode = LccConstants.SuccessEnum.SUCCESS_ZERO.getCode();

    /**
     * 是否打印返回日志
     * 增加此字段因为如果返回内容过多导致日志文件过大,比如Embedding
     */
    @Builder.Default
    private Boolean isPrintResultLog = true;

    /**
     * 多个header
     */
    private Map<String, List<String>> headers = Maps.newHashMap();


}
