# Release Notes

### Version 2.0.1
- **Developer Name** : Vaibhav Choudhary
- **Krista Service APIs Java** : 1.0.115
- **Global Catalog Version** : GC.2025.02.02

**Release Date**: November 4, 2024

### Overview
Major version update migrating the extension to Java 21 with Jakarta EE standards and updated dependencies for improved performance and security.

### What's New

#### Java 21 Migration
- ✅ Migrated from Java 11 to Java 21
- ✅ Updated to Jakarta EE namespace (jakarta.* instead of javax.*)
- ✅ Leveraged Java 21 features for better performance
- ✅ Improved bytecode optimization

#### Dependency Updates
- ✅ Krista SDK upgraded from 1.0.94 to 1.0.115
- ✅ Jersey upgraded from 2.35 to 3.1.3 (Jakarta EE compatible)
- ✅ HK2 upgraded from 2.6.1 to 3.0.3
- ✅ Apache HttpClient upgraded from 4.5.13 to 5.3
- ✅ JUnit upgraded from 5.9.2 to 5.10.1
- ✅ Commons Lang3 upgraded to 3.14.0
- ✅ Commons Validator upgraded to 1.9.0
- ✅ Gson upgraded to 2.10.1

#### Code Improvements
- ✅ Added comprehensive Javadoc documentation to all public methods
- ✅ Enhanced error handling and logging
- ✅ Improved code structure and maintainability
- ✅ Added `supportStore = false` attribute to entity annotations

#### Documentation
- ✅ Complete documentation rewrite following best practices
- ✅ Comprehensive catalog request documentation
- ✅ Detailed architecture documentation
- ✅ Step-by-step setup guides
- ✅ Enhanced troubleshooting sections

### Technical Details

| Component | Version |
|-----------|---------|
| Java | 21 |
| Krista SDK | 1.0.115 |
| Jersey | 3.1.3 |
| HK2 | 3.0.3 |
| Jakarta WS-RS API | 3.1.0 |
| Apache HttpClient | 5.3 |
| Global Catalog Version | GC.2025.11.1 |

### Breaking Changes

⚠️ **Important**: This version requires Java 21 runtime

- Java 11 is no longer supported
- Minimum Krista Platform version: 1.0.115
- All `javax.*` imports changed to `jakarta.*`

### Migration Guide

If upgrading from version 2.0.0 or earlier:

1. **Update Java Runtime**: Ensure Java 21 is installed
2. **Update Krista Platform**: Upgrade to version 1.0.115 or higher
3. **Rebuild Extension**: Clean and rebuild the extension
4. **Test Thoroughly**: Verify all catalog requests work as expected
5. **Update Documentation**: Review new documentation structure

### Bug Fixes

- Fixed entity annotation compatibility with Krista SDK 1.0.115
- Improved error messages for better debugging
- Enhanced timestamp parsing with better error handling
- Fixed potential null pointer exceptions in tracking service

### Known Issues

**None reported** - This is a stable release

If you encounter any issues, please:
1. Check the [Troubleshooting](pages/TrackByTrackingNumber.md#troubleshooting) section
2. Verify your configuration in [Extension Configuration](pages/ExtensionConfiguration.md)
3. Review the [Architecture](pages/Architecture.md) documentation
4. Contact support with detailed error logs

### Limitations and Caveats

#### API Limitations
- **DHL Only**: Extension only supports DHL tracking numbers
- **Rate Limits**: Subject to DHL API rate limits (varies by account)
- **Historical Data**: Limited to 90-120 days of tracking history
- **Single Tracking**: No batch tracking support (one tracking number per request)

#### Performance Considerations
- **Response Time**: 300-800ms average (depends on DHL API)
- **Timeout**: 3000ms default timeout
- **Caching**: No built-in caching (implement at application level)
- **Concurrent Requests**: Limited by DHL API rate limits

#### Data Limitations
- **Provider Field**: Always returns "DHL" regardless of actual carrier
- **Latest Event Only**: Only most recent event is used for status
- **Summary Format**: Auto-generated, not customizable
- **Timestamp Precision**: Converted to Unix epoch milliseconds

#### Security Considerations
- **API Key Storage**: Stored encrypted in Krista
- **HTTPS Only**: All API calls use HTTPS
- **No OAuth**: Uses API key authentication only
- **Key Rotation**: Manual process (recommended every 90 days)

### Compatibility

#### Supported Platforms
- ✅ Krista Platform 1.0.115+
- ✅ Java 21
- ✅ DHL Shipment Tracking - Unified API

#### Tested Environments
- ✅ macOS (Apple Silicon and Intel)
- ✅ Linux (Ubuntu 20.04+)
- ✅ Windows 10/11

### Upgrade Path

| From Version | To Version | Upgrade Complexity | Notes |
|--------------|------------|-------------------|-------|
| 2.0.0 | 2.0.1 | Medium | Requires Java 21 migration |
| 1.x | 2.0.1 | High | Major version change, full testing required |

---

## Version 2.0.0

**Release Date**: Prior to November 2024

### Features
- Initial release with Java 11
- DHL tracking integration
- Track by Tracking Number catalog request
- Basic error handling
- DHL API key authentication

### Technical Details
- Java 11
- Krista SDK 1.0.94
- Jersey 2.35
- HK2 2.6.1

---

## Future Roadmap

### Planned Features
- 🔄 Bulk tracking operations
- 🔄 Tracking history retrieval
- 🔄 Webhook support for status updates
- 🔄 Enhanced caching mechanisms
- 🔄 Additional carrier support

### Under Consideration
- Multi-carrier tracking support
- Advanced filtering and search
- Custom notification rules
- Analytics and reporting
- Mobile SDK integration

---

## Support and Feedback

### Getting Help
- **Documentation**: Review comprehensive documentation in this portal
- **Troubleshooting**: Check troubleshooting sections in each guide
- **Architecture**: Understand technical details in [Architecture](pages/Architecture.md)

### Reporting Issues
When reporting issues, please include:
- Extension version (2.0.1)
- Krista Platform version
- Java version 
- Error messages and stack traces
- Steps to reproduce
- Expected vs actual behavior

### Feature Requests
We welcome feature requests! Please provide:
- Use case description
- Expected behavior
- Business value
- Priority level

---

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup guide
- [Creating DHL Application](pages/CreatingDHLApp.md) - API credentials
- [Track by Tracking Number](pages/TrackByTrackingNumber.md) - Main catalog request
