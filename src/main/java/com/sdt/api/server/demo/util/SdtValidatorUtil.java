package com.sdt.api.server.demo.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public final class SdtValidatorUtil {

    private SdtValidatorUtil() {

    }

    /**
     * validator  Validator对象
     */
    private static Validator validator;

    static {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }


    /**
     * 校验pojo
     *
     * @param obj Object对象
     * @return 如果返回为null 则校验通过；其他返回校验信息
     */
    public static String validatePojo(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        if (set.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            sb.append(constraintViolation.getMessage()).append("；");
        }
        return sb.toString();
    }

    /**
     * 校验pojo 属性
     *
     * @param obj      Object对象
     * @param property property 属性
     * @return 如果返回为null 则校验通过；其他返回校验信息
     */
    public static String validatePojoProperty(Object obj, String property) {
        Set<ConstraintViolation<Object>> set = validator.validateProperty(obj, property);
        if (set.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : set) {
            sb.append(constraintViolation.getMessage()).append("；");
        }
        return sb.toString();
    }

    /**
     * @return 获取 Validator 对象
     */
    public static Validator getValidator() {
        return validator;
    }


}
