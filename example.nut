// NorthStar's SQHttp API:
// https://r2northstar.readthedocs.io/en/latest/reference/northstar/httprequests.html
//
// write: [api_url]/write_data
// read: [api_url]/read_data
// override: [api_url]/override
// URL example: localhost:1145/read_data
//
// NorthStar's Json API: EncodeJSON/DecodeJSON

// WRITE/OVERRIDE DATA
table data = {}
// TODO: put your data
// you can serialize non-string type to string as key/value
string json = EncodeJSON(data)
// [api_url]/write_data: send and merge data
// [api_url]/override: send and override data
if (NSHttpPostBody("[api_url]", json)) {
    printt("Success connect to the io api.")
}

//-------------------------------------------------------------

// READ DATA
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

// override data