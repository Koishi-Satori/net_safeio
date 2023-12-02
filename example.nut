// https://r2northstar.readthedocs.io/en/latest/reference/northstar/httprequests.html
// write: [api_url]/write data
// read: [api_url]/read_data

// POST
table data = {}
// TODO: put player id

string json = EncodeJson(data)

if (NSHttpPostBody("[api_url]", json)) {
    printt("Success connect to the io api.")
}

// GET
HttpRequest request
request.method = HttpRequestMethod.GET
request.url = "[api_url]"

void functionref( HttpRequestResponse ) onSuccess = void function ( HttpRequestResponse response ) : ( callback )
{
SpyglassApi_OnQuerySanctionByIdSuccessful( response, callback )
}

void functionref( HttpRequestFailure ) onFailure = void function ( HttpRequestFailure failure ) : ( callback )
{
SpyglassApi_OnQuerySanctionByIdFailed( failure, callback )
}

return NSHttpRequest( request, onSuccess, onFailure )