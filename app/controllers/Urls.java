package controllers;

import gumi.builders.UrlBuilder;

import java.net.MalformedURLException;

import models.Url;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * A RESTful controller for the Url resource.
 * 
 * From an architectural perspective, the resource's representation does not belong here. Instead, this controller's methods should return
 * the entity itself (or a list of entities), which will be serialized based on content negotiation.
 * 
 * @author samit
 */
public class Urls extends Controller {
	/**
	 * Shortens a URL. Input body must contain a valid JSON document that contains a "url" field.
	 * 
	 * TODO: Handle MalformedURLException better by informing the client that the URL is invalid.
	 * 
	 * @return A JSON representation of the Url resource.
	 * @throws MalformedURLException 
	 */
	public static Result shorten() throws MalformedURLException {
		JsonNode jsonInput = request().body().asJson();
		
		if (jsonInput == null || ! jsonInput.has("url")) {
			return badRequest("Invalid input. Please ensure that your request's body contain a valid JSON document and that all required fields have been satisfied.");
		}
		
		String inputUrlString = jsonInput.findPath("url").getTextValue();

		// TODO: Find a better way to handle the URL format/validation.
		if ( ! inputUrlString.startsWith("http://") && ! inputUrlString.startsWith("https://")) {
			inputUrlString = "http://" + inputUrlString;
		}
		
		// TODO: Replace this with an instance of java.net.URL.
		UrlBuilder urlBuilder = UrlBuilder.fromString(inputUrlString);
		
		// TODO: Confirm that the URL's hostname can be resolved to an IP address or make an actual
		//       connection and confirm that the page exists.
		if (urlBuilder.hostName == null || ! urlBuilder.hostName.contains(".")) {
			throw new MalformedURLException("The 'url' field must contain a valid URL.");
		}
		
		inputUrlString = urlBuilder.toString();
		
		Url url = Url.findByUrl(inputUrlString);
		
		if (url == null) {
			url = new Url(inputUrlString);
			url.save();
		}
		
		return ok(Json.toJson(url));
	}
	
	/**
	 * Expands a URL.
	 * 
	 * TODO: Improve 404 HTML page.
	 * 
	 * @param id
	 * @return 301 redirect status-code (for browsers) or a JSON document.
	 */
	public static Result expend(Long id) {
		Url url = Url.find.byId(id);
		
		if (url == null) {
			return notFound();
		}

		// TODO: What about "application/xhtml+xml"?
		if ( ! request().accepts("text/html") && request().accepts("application/json")) {
			return ok(Json.toJson(url));
		}
		
		// The client supports "text/html", let's redirect it.
		return movedPermanently(url.url);
	}
	
	/**
	 * Informs the user that the requested operation is not implemented.
	 * 
	 * TODO: Add a JSON representation of this 501 page, Play Framework returns a "text/html" page even though 
	 *       the "Accept" header only contains "application/json". Note: In the meantime, clients can use the "Status" header.
	 *  
	 * @see play.mvc.Results.Todo
	 * @return Play Framework's default 501 "Not Implemented" page.
	 */
	public static Result notImplemented() {
		return TODO;
	}
}
