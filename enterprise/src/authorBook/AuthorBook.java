package authorBook;

import java.math.BigDecimal;

import author.Author;
import book.Book;

public class AuthorBook {
	Author myAut;
	Book myBook;
	BigDecimal royalty;
	boolean newRecord = true;
	
	public AuthorBook() {
		royalty = new BigDecimal(0);
	}
	
	//Accesors
	public Author getMyAut() {
		return myAut;
	}
	public void setMyAut(Author myAut) {
		this.myAut = myAut;
	}
	public Book getMyBook() {
		return myBook;
	}
	public void setMyBook(Book myBook) {
		this.myBook = myBook;
	}
	public BigDecimal getRoyalty() {
		return royalty;
	}
	public void setRoyalty(BigDecimal royalty) {
		this.royalty = royalty;
	}
	public boolean isNewRecord() {
		return newRecord;
	}
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	public String toString() {
		return "Author: " + myAut.toString() + "		Royalty: " + getRoyalty().toString();
	}
}
