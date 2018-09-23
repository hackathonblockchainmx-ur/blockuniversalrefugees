/**
 * 
 */
package blockchain.rest.server.models;

import java.math.BigInteger;

/**
 * @author gerardo
 *
 */
public class Document {
	private String id;
	private String hashId;
	private String contents;

	public Document(BigInteger documentId, String ipfsHashId) {
		id = documentId.toString();
		hashId = ipfsHashId;
	}

	public Document() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the hashId
	 */
	public String getHashId() {
		return hashId;
	}

	/**
	 * @param hashId
	 *            the hashId to set
	 */
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents
	 *            the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

}
