package odeen.exam0;

import java.util.ArrayList;

/**
 * Created by Женя on 23.12.2014.
 */
public class Value {
    private double mValue;
    private String mName;
    private Long mId;


    public Value(){}
    public Value(String mName, double mValue, Long mId) {
        this.mValue = mValue;
        this.mName = mName;
        this.mId = mId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double mValue) {
        this.mValue = mValue;
    }
}
