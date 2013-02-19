# URL Shortener
---------------------------------------


## Project details:

### Original description / requirements given:
> We'd like you to build a website that functions as a URL Shortener.  A user should be able to load the index page of your site and be presented with an input field where they can enter a URL. Upon entering the URL, a "shortened" version of that url is created and shown to the user as a URL to the site you're building.  When visiting that "shortened" version of the URL, the user is redirected to the original URL.


> For example, if I enter [http://example.com](http://example.com) into the input field, and I'm running the app locally on port 9000, I'd expect to be given back a URL that looked something like [http://localhost:9000/1](http://localhost:9000/1). When I visit [http://localhost:9000/1](http://localhost:9000/1), I am redirected to [http://example.com](http://example.com).


> Additionally, if a URL has already been shortened by the system, and it is entered a second time, the first shortened URL should be given back to the user. Also invalid URLs should not be accepted.


### Requirements (based on the original description above):
* Web-based (really?)
* The home-page should contain a form with an input field which upon submission, a short url is returned.
* When a client requests the "short url", it will be redirected to the "long url".
* If a "long url" already exists in the system, we should simply return it (instead of creating an additional record).
* Input-URLs (or "long URLs") should be validated.

### Original description notes:
* Port 9000 is used by Play Framework. I've confirmed this with the project's manager and apparently, the development team already uses Play Framework internally. Nice catch.
* According to the example, it's OK to use a standard auto-increment integer as the resource's identifier. In my opinion, this should be avoided for the following reasons:
  * As a principle, I believe information should be provided on a need-to-know basis (this referrs to *all* information). In this case, auto-increment isn't exactly a security risk but as an example, it could be used by the competition to determine the amount of URL we've shortened, which translates directly to the success of this project.
	* Auto-increment is generally harder to scale. Generally, it means that operations must be atomic (to prevent PK collisions) which slows the insert rate and rules out the use of most "eventually-consistent" databases.
	* If I had to replace the auto-increment PK generation strategy, I would either use a hashing algorithm that provides a very short output or simply use random, alphanumeric characters (which is more likely).
	* Since the points above are not so important for the sake of this example project, I'm not going to implement any other PK generation strategy at this point.
* After considering which redirect status-code to use (permanent or temporary), I've decided to go with permanent, because  it would be more efficient for clients/proxies (due to caching) as well as SEO-friendly (we don't want people to hesitate too much before they use our service).


### Entity Model
* Url
	* Fields:
		* `id` contains a unique identifier, which will be used as the URI for the "short url".
		* `url` contains the "long url".
	* Constrains:
		* The `url` property MUST NOT be `null`.
		* The `url` property MUST NOT be empty.
		* The `url` property MUST be a valid URL.
		* The `url` property MUST NOT be an already-shortened URL (by this service).
	* Notes:
		* Make sure to trim the URL string for whitespace characters prior to any other validation rules.
		* The `url` not-empty constrain should be covered by the URL-validation (but should still be tested for).
		* A `url` with two slashes should not differ from a `url` with one slash (i.e.: `http://example.com//contact` and `http://example.com/contact` should be considered as the same URL).
		* While a simple pattern-based URL validation works, we should definitely consider to validate the URL by making an actual request and make sure we do not receive a (permanent) error status-code.




## Implementation details:
In this example, I chose to implement the server-side (the client-side could be completely separated) with Play Framework mainly because it's used internally by the development team (see "original description notes" above), however, while I like Play Framework for so many reasons, I think a `JAX-RS`-based implementation would be a better tool for the (RESTful API) job, therefore, I'll be providing an additional server-side implementation based on `Jersey` (which is a `JAX-RS` implementation).

### Server-Side:

#### Resources and operations:
* Url
	* shorten (takes a `longUrl` argument, returns the associated "shortened URL").
	* expand (takes a `shortId` argument, returns the associated "long URL").


#### Routes:
* `GET /` routes to our basic HTML client.
* Resource: `Url`
	* `POST /url` routes to the `shorten` operation.
	* `GET /url/:id` routes to the `expand` operation.
	* `GET /url` will return a 501 status-code ("not implemented").
	* `PUT /url/:id` will return a 501 status-code ("not implemented").
	* `DELETE /url/:id` will return a 501 status-code ("not implemented").
	


### Client-Side:
The basic client for this application is very simplistic, therefore, I'm only planning on using basic tools/libraries.

#### Libraries we'll be using:
* Twitter Bootstrap: To give our single-page "app" the needed components/layout/style.
* jQuery: To allow us traverse/manipulate DOM and make "AJAX" requests using a simple and elegant API.

#### Other notes to be added to this document
* Unit-testing
* Improve server-side validation
* Implement client-side validation
* Client-side performance (combine/minify CSS/JS code)
