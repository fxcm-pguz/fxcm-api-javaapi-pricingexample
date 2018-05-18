/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapipricelistener;

import com.fxcm.external.api.transport.FXCMLoginProperties;
import com.fxcm.external.api.transport.GatewayFactory;
import com.fxcm.external.api.transport.IGateway;
import com.fxcm.external.api.transport.listeners.IGenericMessageListener;
import com.fxcm.external.api.transport.listeners.IStatusMessageListener;
import com.fxcm.fix.pretrade.MarketDataSnapshot;
import com.fxcm.messaging.ISessionStatus;
import com.fxcm.messaging.ITransportable;

/**
 *
 * @author pguz
 */
public class JavaAPIPriceListener implements IGenericMessageListener, IStatusMessageListener {

	private final IGateway gateway;
	private FXCMLoginProperties loginProperties;

	private String username;
	private String password;
	private String station;
	private String server;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		JavaAPIPriceListener japl = new JavaAPIPriceListener();
		japl.loadConfig();
		japl.login();
		System.in.read();
	}

	public JavaAPIPriceListener() {
		this.gateway = GatewayFactory.createGateway();
	}

	public boolean loadConfig() {
		return loadConfig("d161038313", "1234", "Demo", "http://www.fxcorporate.com/Hosts.jsp");
	}

	public boolean loadConfig(String username, String password, String station, String server) {
		this.username = username;
		this.password = password;
		this.station = station;
		this.server = server;

		this.loginProperties = new FXCMLoginProperties(
						this.username,
						this.password,
						this.station,
						this.server);
		return true;
	}

	public boolean login() throws Exception {
		this.gateway.registerGenericMessageListener(this);
		this.gateway.registerStatusMessageListener(this);
		this.gateway.login(loginProperties);
		this.gateway.requestTradingSessionStatus();
		return this.gateway.isConnected();
	}

	@Override
	public void messageArrived(ITransportable it) {
		System.out.println(it.toString());
//		if (it instanceof MarketDataSnapshot) {
//			this.messageArrived((MarketDataSnapshot) it);
//		}
	}

	public void messageArrived(MarketDataSnapshot mds) {
		System.out.println(mds.toString());
	}

	@Override
	public void messageArrived(ISessionStatus iss) {
		// do nothing
	}

}
