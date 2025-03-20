package ca.qzic.dsp.util;

/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author Quentin
 */
public interface Command {
    public boolean execute(double data[]);
    public boolean execute(int data[]);
}
