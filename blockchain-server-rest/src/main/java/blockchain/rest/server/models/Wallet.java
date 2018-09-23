/**
 * 
 */
package blockchain.rest.server.models;

/**
 * @author gerardo
 *
 */
public class Wallet {
	private String address;
	
	public Wallet(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
