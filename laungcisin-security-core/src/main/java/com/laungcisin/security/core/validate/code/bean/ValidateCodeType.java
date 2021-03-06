package com.laungcisin.security.core.validate.code.bean;


import com.laungcisin.security.core.properties.SecurityConstants;

/**
 * @author laungcisin
 */
public enum ValidateCodeType {

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }

        @Override
        public String getTypeName() {
            return SecurityConstants.DEFAULT_TYPE_NAME_CODE_IMAGE;
        }
    },

    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }

        @Override
        public String getTypeName() {
            return SecurityConstants.DEFAULT_TYPE_NAME_CODE_SMS;
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     *
     * @return
     */
    public abstract String getParamNameOnValidate();

    /**
     * 类型名称
     *
     * @return
     */
    public abstract String getTypeName();
}
