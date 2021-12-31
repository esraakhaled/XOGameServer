package xogameserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Raiaan
 */
public abstract class RoutingBase {
    static final int login = 1;
    static final int register = 2;
    int route;

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }
    
}
