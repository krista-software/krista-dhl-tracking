[comment]: <> (Home page)

# Krista DHL Tracking and Delivery Extension

## Overview

The Krista DHL Tracking and Delivery Extension integrates with DHL's tracking API to provide real-time shipment tracking capabilities within the Krista platform. Track packages, retrieve detailed shipping information, and monitor delivery status using DHL tracking numbers.

**Version:** 2.0.1
**Domain:** Tracking and Delivery
**Ecosystem:** Logistics

---

## Key Features

✅ **Real-Time Tracking** - Query DHL shipments using tracking numbers
✅ **Comprehensive Information** - Get detailed status, location, and event history
✅ **Secure Authentication** - API key-based authentication with DHL services
✅ **Error Handling** - Robust validation and error management
✅ **Easy Configuration** - Simple setup with DHL API credentials
✅ **Production Ready** - Built on Java 21 with Jakarta EE standards

---

## Quick Start Guide

### 1. Prerequisites
- Active DHL Developer Account
- DHL API Key with tracking permissions
- Krista Platform 1.0.115 or higher

### 2. Setup Steps
1. [Create DHL Developer Account and Application](pages/CreatingDHLApp.md)
2. [Configure Extension in Krista](pages/ExtensionConfiguration.md)
3. Test connection and start tracking shipments

### 3. Start Tracking
Use the **Track by Tracking Number** catalog request to retrieve shipment information.

---

## Documentation Structure

### Getting Started
- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration guide
- [Creating DHL Application](pages/CreatingDHLApp.md) - Obtain API credentials
- [Architecture](pages/Architecture.md) - Technical architecture and design

### Catalog Requests
- **Tracking Operations**
  - [Track by Tracking Number](pages/TrackByTrackingNumber.md) - Query shipment status

### Reference
- [Release Notes](pages/ReleaseNotes.md) - Version history and changes

---

## Support & Resources

- **Extension Version:** 2.0.1
- **Java Version:** 21
- **Krista SDK:** 1.0.115
- **DHL API:** [Developer Documentation](https://developer.dhl.com/)

---

## Need Help?

- Review the [Architecture Documentation](pages/Architecture.md) for technical details
- Check [Troubleshooting](pages/Architecture.md#error-scenarios) for common issues
- Consult the [DHL API Documentation](https://developer.dhl.com/) for API-specific questions
