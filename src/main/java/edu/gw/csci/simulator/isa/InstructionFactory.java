package edu.gw.csci.simulator.isa;

import java.lang.reflect.InvocationTargetException;

public class InstructionFactory<I extends Instruction> {

    private Class<I> clazz;

    public InstructionFactory(Class<I> clazz){
        this.clazz = clazz;
    }

    public I create(String data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        I instance = clazz.getDeclaredConstructor().newInstance();
        instance.setData(data);
        return instance;
    }
}
