(function($) {	// Create a private scope (with a reference to jQuery).
	// This will give us better error reporting.
	// @see http://ejohn.org/blog/ecmascript-5-strict-mode-json-and-more/
	"use strict";
	
	var SUCCESS_CLASS = 'shortening-success';
	
	/**
	 * TODO: Add client-side validation. Let's not wait for the server's response in case of an invalid URL.
	 * TODO: Add JSON.stringify to < IE8 (see note in `main.scala.html`).
	 */
	function shorten(url) {
		return $.ajax({
			url: '/url',
			type: 'POST',
			contentType: 'application/json; charset=UTF-8',
			data: JSON.stringify({ url: url })
		});
	}
	
	$(function() { // DOM is ready.
		var homeContainer = $('.home-container'),
			errorEl = $('.alert', homeContainer),
			shortenUrlEl = $('#shorten-url'),
			shortenFormEl = $('#shorten-form'),
			shorteningResultEl = $('#shortening-result'),
			shortenedUrlEl = $('#shortened-url')
			;
		
		function reset() {
			// We could just use shortenFormEl[0] but I prefer to follow jQuery's API.
			shortenFormEl.each(function() {
		        this.reset();
			});
			
			homeContainer.removeClass(SUCCESS_CLASS);
			
			shortenedUrlEl.val('');
		}
		
		/**
		 * Will be called when the server returned a success status-code.
		 */
		function onShortenDone(data) {
			shortenedUrlEl.val(window.location.href + data.id);
			homeContainer.addClass(SUCCESS_CLASS);
		}
		
		/**
		 * Will be called when the server returned an error status-code.
		 * 
		 * TODO: Improve error handling based on the server's response-code (and error message once
		 *       our API can produce an error message based on the consumed content-type).
		 */
		function onShortenFail() {
			errorEl.show();
		}
		
		/**
		 * TODO: Add error handling in case our URL is invalid.
		 * TODO: Show a "progress" indicator until we get a response back from the server.
		 */
		shortenFormEl.submit(function() {
			errorEl.hide();
			
			shorten(shortenUrlEl.val()).done(onShortenDone).fail(onShortenFail);
			
			// Prevent the browser's default submit action.
			return false; 
		});
		
		$('#shorten-another').click(function() {
			reset();
			return false;
		});
	});
})(jQuery);