package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.URL;

import play.data.format.Formats.NonEmpty;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Url extends Model {
	private static final long serialVersionUID = 1731896256463754963L;

	@Id
	public Long id;
	
	@Required
	@NonEmpty
	public String url;
	
	public Url(String url) {
		this.url = url;
	}
	
	// ---------- STATIC HELPERS ----------

	/**
	 * A generic Play Framework / Ebean finder.
	 */
	public static Finder<Long, Url> find = new Finder<Long, Url>(Long.class, Url.class);
	
	/**
	 * Finds a single Url record by the "long URL" value.
	 * 
	 * @param url The long URL
	 * @return An instance of Url or null
	 */
	public static Url findByUrl(String url) {
		if (url == null || url.length() == 0) {
			throw new IllegalArgumentException("The 'url' argument is invalid.");
		}
		
		url = url.toLowerCase();
		
		return find.where().eq("url", url).findUnique();
	}
}
