package com.laungcisin.security.core.validate.code.repository;

import com.laungcisin.security.core.validate.code.bean.ValidateCode;
import com.laungcisin.security.core.validate.code.bean.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {

    /**
     * 保存校验码
     *
     * @param request
     * @param code
     * @param validateCodeType
     * @return
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /**
     * 获取校验码
     *
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    /**
     * 移除校验码
     *
     * @param request
     * @param codeType
     * @return
     */
    void remove(ServletWebRequest request, ValidateCodeType codeType);
}
