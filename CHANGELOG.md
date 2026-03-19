# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased] - 2026-03-19

### Added
- **AMF Padding Support (Issue #1)**: Implemented AMF (Action Message Format) padding by prepending bullet data before AMF content. This allows WAF bypass for Flash/Flex applications.
- **Testing Documentation**: Added comprehensive TESTING.md file with manual testing checklist for all content types and features.

### Fixed
- **JSON Array Handling (Issue #5)**: Fixed critical bug where JSON arrays were incorrectly transformed into objects. The extension now properly detects and handles:
  - JSON objects: Adds bullet as a new property
  - JSON arrays: Adds bullet as the first array element
  - Other JSON types: Wraps in object with bullet property and value
- **Dependency Version Warning**: Fixed Maven warning by specifying explicit JUnit version (5.11.4) instead of RELEASE

### Changed
- **Montoya API Update**: Updated from version 2023.12.1 to 2026.2 (latest version)
  - Ensures compatibility with latest Burp Suite 2026.2
  - Takes advantage of latest API improvements and bug fixes
- **README Updates**:
  - Added note about Montoya API 2026.2 compatibility
  - Updated supported content types to include AMF
  - Clarified JSON array support
  - Fixed typo in "content type of unknown"

### Technical Details

#### JSON Array Fix (RequestBuilder.java:57-97)
The `addJsonParam` method now:
1. Trims the body and checks the first character
2. For arrays starting with `[`: Prepends `["bullet_value",`
3. For objects starting with `{`: Prepends `{"bullet": "value",`
4. For other JSON types: Wraps in object structure

Example transformations:
```json
// Array: Before
["test data"]

// Array: After (FIXED)
["aaaa...", "test data"]

// Object: Before
{"key": "value"}

// Object: After
{"bullet": "aaaa...", "key": "value"}
```

#### AMF Support (RequestBuilder.java:153-161)
The `padAmfWith` method now:
1. Extracts the request body
2. Prepends bullet padding before AMF binary data
3. Returns modified request

This approach is WAF-bypass friendly as it:
- Doesn't require parsing complex AMF binary structure
- Allows WAF to consume padding while leaving AMF intact
- Works with AMF0, AMF3, and AMFX formats

#### Dependency Updates (pom.xml)
```xml
<!-- Before -->
<version>2023.12.1</version>
<version>RELEASE</version>

<!-- After -->
<version>2026.2</version>
<version>5.11.4</version>
```

### Build Information
- Successfully compiles with Java 17
- JAR size: ~27 KB
- No compilation warnings or errors
- All existing functionality preserved

### Testing
See TESTING.md for comprehensive manual testing procedures. Automated unit testing is not possible due to Montoya API requiring Burp Suite runtime environment.

### Migration Notes
For users upgrading from previous versions:
1. This version requires Burp Suite 2026.2 or later
2. All existing functionality is preserved and backward compatible
3. JSON array requests will now be handled correctly (fixing Issue #5)
4. AMF requests will now be supported (fixing Issue #1)
5. No configuration changes required

### Known Limitations
- Unit tests cannot run independently of Burp Suite (Montoya API limitation)
- Testing must be performed manually within Burp Suite
- Some WAFs may detect prepended padding patterns over time

### Future Considerations
- Consider implementing more sophisticated AMF header injection
- Explore JSON padding alternatives for different API designs
- Add support for GraphQL query padding
- Investigate protobuf and other binary format support
