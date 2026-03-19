# Testing Guide

This extension requires the Burp Suite runtime environment to function properly. The Montoya API is not designed for unit testing outside of Burp Suite.

## Manual Testing Checklist

When testing this extension in Burp Suite, verify the following:

### 1. JSON Object Handling
- **Test**: Send a POST request with JSON object body: `{"key": "value"}`
- **Expected**: Bullet should be added as a new property: `{"bullet": "aaa...", "key": "value"}`

### 2. JSON Array Handling (Issue #5)
- **Test**: Send a POST request with JSON array body: `["test data"]`
- **Expected**: Bullet should be added as first array element: `["aaa...", "test data"]`
- **Note**: Should NOT wrap the array in an object

### 3. URL-Encoded Body
- **Test**: Send a POST request with URL-encoded body: `param1=value1`
- **Expected**: Bullet parameter should be prepended: `bullet=aaa...&param1=value1`

### 4. XML Body
- **Test**: Send a POST request with XML body: `<?xml version="1.0"?><root><data>test</data></root>`
- **Expected**: Bullet should be added as XML comment after first tag

### 5. Multipart Body
- **Test**: Send a POST request with multipart form data
- **Expected**: Bullet should be added as first form field with name "bullet"

### 6. AMF Body (Issue #1)
- **Test**: Send a POST request with AMF (application/x-amf) content type
- **Expected**: Bullet should be prepended to the AMF binary data
- **Note**: AMF support is now implemented

### 7. Unknown Content Type
- **Test**: Send a POST request with unknown content type
- **Expected**: Bullet should be prepended to the body (best effort)

### 8. Manual Insert
- **Test**: Use "Insert Bullet at Caret" from context menu
- **Expected**: Bullet should be inserted at cursor position or replace selection

### 9. Auto Add
- **Test**: Use "Add Bullet as First Parameter" from context menu
- **Expected**: Bullet should be added as first parameter based on content type

### 10. Active Scanner Integration
- **Test**: Run active scan with extension enabled
- **Expected**: Additional scan checks should be created with bullets of sizes: 8, 16, 32, 64, 128, 1024 KB

## Regression Testing

After updates, ensure:
1. Extension loads without errors in Burp Suite
2. All content types are handled correctly
3. No JavaScript/JSON syntax errors in modified requests
4. Active scanner integration works as expected
5. Context menu items appear and function correctly

## Known Limitations

- The Montoya API requires Burp Suite runtime to create HttpRequest objects
- Unit tests cannot be run independently of Burp Suite
- Testing must be performed manually within Burp Suite or with a Burp Suite test harness
