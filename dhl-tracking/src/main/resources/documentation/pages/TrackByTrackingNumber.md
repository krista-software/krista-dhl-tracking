# Track by Tracking Number

## Overview

Retrieves comprehensive tracking information for a DHL shipment using its tracking number, including package status, location, event history, and shipping details.

## Request Details

- **Area**: Tracking
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes - This is an idempotent read operation that can be safely retried without side effects

> **📝 Note**: This request does not modify any data and is safe to retry in case of network timeouts or transient failures.

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Tracking Number | Identifier | Yes | The DHL tracking number to query | `2042611047314400` |

### Parameter Details

#### Tracking Number
- **Type**: Identifier (text)
- **Required**: Yes
- **Visual Width**: S (Small)
- **Format**: Typically 10-11 digits
- **Validation**: Must not be null or empty

**Valid Examples**:
- `2042611047314400`
- `1234567890`
- `7305678001806`

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Tracking Information | Entity(Tracking Info) | Complete tracking information for the shipment |

### Tracking Info Entity Structure

| Field Name | Type | Required | Description |
|------------|------|----------|-------------|
| Tracking Number | Text | Yes | The DHL tracking number queried |
| Provider | Text | Yes | Shipping provider (always "DHL") |
| Last Event Date and Time | Date | Yes | Timestamp of the most recent tracking event (Unix epoch milliseconds) |
| Last Event | Text | Yes | Description of the most recent event |
| Last Status | Text | Yes | Current status of the shipment |
| Last Location | Text | Yes | Current or last known location (city/region) |
| Summary | Rich Text | Yes | Formatted multi-line summary of shipment details |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Tracking number is null or empty | `"Empty Tracking Number"` | Provide a valid tracking number |
| Tracking number contains only whitespace | `"Empty Tracking Number"` | Provide a non-empty tracking number |
| Invalid tracking number format | `"Unable to Track, provide correct Tracking Number"` | Verify tracking number is correct and belongs to DHL |

## Error Handling

### Input Errors (INPUT_ERROR)

#### Empty or Null Tracking Number
**Cause**: Tracking number parameter is null, empty, or contains only whitespace

**Error Message**: `"Empty Tracking Number"`

**Exception**: `NullPointerException`

**Common Scenarios**:
- User submits form without entering tracking number
- Tracking number field contains only spaces
- Null value passed to the request

**Resolution**:
1. Ensure tracking number field is populated
2. Validate input before calling the request
3. Trim whitespace from user input
4. Display clear error message to user

---

### Logic Errors (LOGIC_ERROR)

#### Invalid Tracking Number
**Cause**: Tracking number not found in DHL system or invalid format

**Error Message**: `"Unable to Track, provide correct Tracking Number"`

**Exception**: `IllegalArgumentException`

**HTTP Status**: 404 Not Found (from DHL API)

**Common Scenarios**:
- Tracking number doesn't exist in DHL system
- Tracking number belongs to different carrier
- Typo in tracking number
- Shipment not yet in DHL system

**Resolution**:
1. Verify tracking number is correct
2. Check if shipment exists in DHL system
3. Ensure tracking number belongs to DHL (not FedEx, UPS, etc.)
4. Wait a few hours if shipment was just created
5. Contact sender to verify tracking number

---

### System Errors (SYSTEM_ERROR)

#### Authentication Failure
**Cause**: Invalid or expired API key

**Error Message**: `"Invalid Login Credentials"`

**Exception**: `RuntimeException`

**HTTP Status**: 401 Unauthorized

**Common Scenarios**:
- API key is incorrect or expired
- API key doesn't have tracking permissions
- DHL account is suspended

**Resolution**:
1. Verify API key in extension configuration
2. Test connection from extension settings
3. Regenerate API key from DHL Developer Portal
4. Ensure API key has "Shipment Tracking - Unified" API enabled
5. Check DHL account status

---

#### Network Timeout
**Cause**: DHL API not responding within timeout period

**Error Message**: Connection timeout or read timeout

**Exception**: `jakarta.ws.rs.ProcessingException`

**HTTP Status**: N/A

**Common Scenarios**:
- DHL API is experiencing high load
- Network connectivity issues
- Firewall blocking requests

**Resolution**:
1. Check network connectivity
2. Verify DHL API status at [https://developer.dhl.com/](https://developer.dhl.com/)
3. Retry request after a short delay
4. Check firewall settings
5. Contact network administrator if issue persists

---

#### Rate Limit Exceeded
**Cause**: Too many requests to DHL API in short time period

**Error Message**: Rate limit exceeded

**Exception**: `ClientErrorException`

**HTTP Status**: 429 Too Many Requests

**Common Scenarios**:
- Bulk tracking operations
- Multiple concurrent requests
- Exceeded DHL API rate limits

**Resolution**:
1. Implement request throttling
2. Add delays between requests (e.g., 100-200ms)
3. Contact DHL to increase rate limits for your API key
4. Cache results to reduce API calls
5. Use batch processing with delays

---

#### DHL API Error
**Cause**: DHL API returned an error response

**Error Message**: Varies based on DHL API response

**Exception**: `ClientErrorException`

**HTTP Status**: 4xx or 5xx

**Common Scenarios**:
- DHL API maintenance
- DHL API service disruption
- Invalid API request format

**Resolution**:
1. Check DHL API status
2. Review DHL API documentation
3. Retry after a delay
4. Contact DHL support if issue persists

## Usage Examples

### Example 1: Successful Tracking

**Scenario**: Track a delivered package

**Input**:
```
Tracking Number: "2042611047314400"
```

**Output**:
```json
{
  "trackingNumber": "2042611047314400",
  "provider": "DHL",
  "lastEventDateAndTime": 1699123456789,
  "lastEvent": "Delivered",
  "lastStatus": "delivered",
  "lastLocation": "New York",
  "summary": "The package 2042611047314400\nWas sent by DHL Express\nWeight 2.5 kg\nCurrent Status: Delivered at 2024-11-04T14:30:00"
}
```

**Result**: Successfully retrieved tracking information showing package was delivered in New York

---

### Example 2: Package In Transit

**Scenario**: Track a package currently being shipped

**Input**:
```
Tracking Number: "7305678001806"
```

**Output**:
```json
{
  "trackingNumber": "7305678001806",
  "provider": "DHL",
  "lastEventDateAndTime": 1699100000000,
  "lastEvent": "In transit",
  "lastStatus": "transit",
  "lastLocation": "Chicago",
  "summary": "The package 7305678001806\nWas sent by DHL Express\nWeight 1.2 kg\nCurrent Status: In transit at 2024-11-03T18:00:00"
}
```

**Result**: Successfully retrieved tracking information showing package is in transit through Chicago

---

### Example 3: Retry Scenario

**Scenario**: Network timeout on first attempt, successful on retry

**First Attempt**:
```
Tracking Number: "2042611047314400"
Result: ProcessingException (timeout)
```

**Retry Attempt** (after 2 second delay):
```
Tracking Number: "2042611047314400"
Result: Success - Tracking information retrieved
```

**Result**: Request succeeded on retry, demonstrating idempotent nature of the operation

## Business Rules

1. **Provider is Always DHL**: The provider field will always be set to "DHL" regardless of the actual carrier
2. **Latest Event First**: The most recent tracking event is always used for status and location
3. **Timestamp Format**: All timestamps are converted to Unix epoch milliseconds for consistency
4. **Summary Generation**: Summary is automatically generated from shipment details and cannot be customized
5. **Read-Only Operation**: This request does not modify any data in DHL systems
6. **Single Shipment**: Only the first shipment is returned if multiple shipments match the tracking number

## Limitations

1. **DHL Only**: This extension only works with DHL tracking numbers, not other carriers
2. **API Rate Limits**: Subject to DHL API rate limits (varies by account type)
3. **Historical Data**: Limited to tracking data available in DHL's system (typically 90-120 days)
4. **Real-Time Updates**: Tracking information may have a delay of 5-15 minutes from actual events
5. **No Batch Tracking**: Each tracking number must be queried individually
6. **Authentication Required**: Requires valid DHL API key with tracking permissions

## Best Practices

### 1. Input Validation
Always validate and sanitize tracking numbers before calling the request:
- Trim whitespace from user input
- Check for null or empty values
- Validate format (typically numeric, 10-11 digits)
- Provide clear error messages to users

### 2. Error Handling
Implement comprehensive error handling:
- Catch specific exceptions (NullPointerException, IllegalArgumentException, RuntimeException)
- Log errors for debugging and monitoring
- Provide user-friendly error messages
- Implement retry logic for transient failures (timeouts, rate limits)

### 3. Performance Optimization
Optimize for better performance:
- Cache tracking results for 5-10 minutes to reduce API calls
- Implement request throttling to avoid rate limits
- Use async processing for bulk tracking operations
- Monitor API response times and set appropriate timeouts

### 4. Security
Follow security best practices:
- Never log or expose API keys
- Use HTTPS for all API calls
- Validate SSL certificates
- Sanitize tracking numbers to prevent injection attacks
- Store API keys securely in extension configuration

### 5. User Experience
Enhance user experience:
- Display loading indicators during API calls
- Show clear status messages (tracking, success, error)
- Format tracking information for readability
- Provide retry option for failed requests
- Display timestamp in user's local timezone

## Common Use Cases

### 1. Customer Service Portal
```
Scenario: Customer service representative needs to check package status
Action: Enter customer's tracking number in Krista application
Result: Display current location, status, and delivery estimate to customer
```

### 2. Automated Status Updates
```
Scenario: Send automated email updates when package status changes
Action: Query tracking information periodically (every 30 minutes)
Result: Detect status changes and trigger email notifications
```

### 3. Delivery Dashboard
```
Scenario: Display real-time delivery status for multiple packages
Action: Query tracking information for all active shipments
Result: Show consolidated view of all package statuses and locations
```

## Related Catalog Requests

Currently, this is the only catalog request in the DHL Tracking Extension. Future enhancements may include:
- Bulk tracking operations
- Tracking history retrieval
- Delivery notifications

## Technical Implementation

### Helper Class
- **Class**: TrackingServiceImplementation
- **Package**: `com.krista.extensions.logistics.trackinganddelivery.dhl.service.implementation`
- **Validation**: Checks if tracking number is not empty using `StringUtils.isNotEmpty()`
- **Service**: Delegates to `TrackingClient.trackPackage()` for API call

### Implementation Flow
1. **Input Validation**: `TrackingArea.trackByTrackingNumber()` receives tracking number
2. **Configuration**: `setConfig()` initializes service with API credentials
3. **Service Call**: `TrackingServiceImplementation.trackShipment()` validates and processes request
4. **API Call**: `TrackingClient.trackPackage()` makes HTTP GET request to DHL API
5. **Response Handling**: `RestClient.get()` handles HTTP response and authentication
6. **Data Transformation**: `trackingInfoParser()` converts Shipment to TrackingInfo entity
7. **Summary Generation**: `parseSummary()` creates formatted summary text
8. **Timestamp Conversion**: `timestampStringToLong()` converts ISO 8601 to Unix epoch
9. **Return**: TrackingInfo entity returned to caller

### API Details
- **Endpoint**: `{baseUrl}/track/shipments`
- **Method**: GET
- **Query Parameter**: `trackingNumber`
- **Authentication**: `DHL-API-Key` header
- **Response Format**: JSON
- **Timeout**: 3000ms (configurable)

### Telemetry Metrics
Currently, this extension does not implement custom telemetry metrics. Standard Krista platform metrics are tracked:
- Request count
- Response time
- Error rate
- Success rate

## Troubleshooting

### Issue: "Empty Tracking Number" Error
**Cause**: Tracking number is null, empty, or whitespace-only

**Solution**:
1. Check that tracking number field is populated
2. Verify no leading/trailing whitespace
3. Ensure tracking number is not null
4. Validate input before submission

---

### Issue: "Unable to Track" Error
**Cause**: Tracking number not found or invalid

**Solution**:
1. Verify tracking number is correct (check for typos)
2. Confirm tracking number belongs to DHL
3. Wait if shipment was recently created (may take 1-2 hours to appear in system)
4. Contact sender to verify tracking number
5. Check if shipment has been archived (older than 120 days)

---

### Issue: "Invalid Login Credentials" Error
**Cause**: API key authentication failed

**Solution**:
1. Go to extension Setup page
2. Click "Test Connection" to verify credentials
3. If test fails, regenerate API key from DHL Developer Portal
4. Update API key in extension configuration
5. Ensure "Shipment Tracking - Unified" API is enabled for your app
6. Click "Validate Attributes" after updating

---

### Issue: Timeout or Slow Response
**Cause**: Network latency or DHL API performance

**Solution**:
1. Check network connectivity
2. Verify DHL API status
3. Retry request after a short delay
4. Consider increasing timeout if consistently slow
5. Implement caching to reduce repeated calls

---

### Issue: Rate Limit Errors
**Cause**: Too many API requests in short time

**Solution**:
1. Implement delays between requests (200-500ms)
2. Cache results for 5-10 minutes
3. Reduce frequency of bulk operations
4. Contact DHL to increase rate limits
5. Monitor API usage patterns

## See Also

- [Extension Configuration](ExtensionConfiguration.md) - Setup and configuration guide
- [Creating DHL Application](CreatingDHLApp.md) - Obtain API credentials
- [Architecture](Architecture.md) - Technical architecture and design
- [DHL API Documentation](https://developer.dhl.com/) - Official DHL API reference

