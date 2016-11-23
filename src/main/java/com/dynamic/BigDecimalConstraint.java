package com.dynamic;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import java.math.BigDecimal;
import java.util.Map;

public class BigDecimalConstraint extends AbstractAnnotationCheck<BigDecimalCheck> {
    protected ConstraintTarget[] getAppliesToDefault() {
        return new ConstraintTarget[]{ConstraintTarget.VALUES};
    }

    private int scale;
    private int precision;

    public BigDecimalConstraint() {
    }

    public boolean isSatisfied(Object o, Object o1, OValContext oValContext, Validator validator) throws OValException {
        BigDecimal field = (BigDecimal) o1;
        if(field == null) return true;
        if(field.scale() > scale || field.precision() > precision) return false;
        return true;
    }

    public void configure(BigDecimalCheck constraintAnnotation) {
        super.configure(constraintAnnotation);
        this.setScale(constraintAnnotation.scale());
        this.setPrecision(constraintAnnotation.precision());
    }

    protected Map<String, String> createMessageVariables() {
        Map messageVariables = Validator.getCollectionFactory().createMap(2);
        messageVariables.put("scale", Integer.toString(this.scale));
        messageVariables.put("precision", Integer.toString(this.precision));
        return messageVariables;
    }


    public void setScale(int max) {
        this.scale = scale;
        this.requireMessageVariablesRecreation();
    }

    public void setPrecision(int min) {
        this.precision = precision;
        this.requireMessageVariablesRecreation();
    }
}
