package net.emandates.merchant.library;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Configuration class
 */
public class Configuration {
    private String eMandateContractId;
    private int eMandateContractSubId;
    private String merchantReturnUrl;
    private String keyStoreLocation;
    private InputStream keyStore;
    private String keyStorePassword;
    private String signingCertificateAlias;
    private String signingCertificatePassword;
    private String acquirerCertificateAlias;
    private String acquirerAlternateCertificateAlias;
    private String acquirerUrl_DirectoryReq;
    private String acquirerUrl_TransactionReq;
    private String acquirerUrl_StatusReq;
    private boolean logsEnabled;
    private boolean serviceLogsEnabled;
    private String serviceLogsLocation;
    private String serviceLogsPattern;
    private ILoggerFactory loggerFactory;
    private boolean tls12Enabled;

    private static Configuration instance;

    /**
     * Gets the default Configuration instance
     * @return the default Configuration instance
     */
    public static Configuration defaultInstance() {
        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }

    private Configuration() {
    }
    
    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStoreLocation A Java keystore (file on the disk) that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerAlternateCertificateAlias A string which specifies the alias of the alternate certificate to use to validate messages 
     * from the creditor bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, String keyStoreLocation,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias, String acquirerAlternateCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, ILoggerFactory loggerFactory) throws IOException {
        
        this(
            eMandateContractId, 
            eMandateContractSubId, 
            merchantReturnUrl, 
            loadKeyStore(keyStoreLocation), // load the InputStream
            keyStorePassword, 
            signingCertificateAlias, 
            signingCertificatePassword, 
            acquirerCertificateAlias,
            acquirerAlternateCertificateAlias,
            acquirerUrl_DirectoryReq, 
            acquirerUrl_TransactionReq, 
            acquirerUrl_StatusReq, 
            logsEnabled, 
            serviceLogsEnabled, 
            serviceLogsLocation, 
            serviceLogsPattern, 
            loggerFactory);
    }
    
    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStore A Java keystore InputStream that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerAlternateCertificateAlias A string which specifies the alias of the alternate certificate to use to validate messages 
     * from the creditor bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, InputStream keyStore,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias, String acquirerAlternateCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, ILoggerFactory loggerFactory) throws IOException {

                this.eMandateContractId = eMandateContractId;
        this.eMandateContractSubId = eMandateContractSubId;
        this.merchantReturnUrl = merchantReturnUrl;
        this.signingCertificateAlias = signingCertificateAlias;
        this.signingCertificatePassword = signingCertificatePassword;
        this.acquirerCertificateAlias = acquirerCertificateAlias;
        this.acquirerAlternateCertificateAlias = acquirerAlternateCertificateAlias;
        this.acquirerUrl_DirectoryReq = acquirerUrl_DirectoryReq;
        this.acquirerUrl_TransactionReq = acquirerUrl_TransactionReq;
        this.acquirerUrl_StatusReq = acquirerUrl_StatusReq;
        this.logsEnabled = logsEnabled;
        this.serviceLogsEnabled = serviceLogsEnabled;
        this.serviceLogsLocation = serviceLogsLocation;
        this.serviceLogsPattern = serviceLogsPattern;
        this.tls12Enabled = false;
        this.loggerFactory = loggerFactory != null ? loggerFactory : new LoggerFactory();

        setKeyStoreAndPass(keyStore, keyStorePassword);
    }
    

    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStoreLocation A Java keystore (file on the disk) that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, String keyStoreLocation,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, ILoggerFactory loggerFactory) throws IOException {
        
        this(
            eMandateContractId, 
            eMandateContractSubId, 
            merchantReturnUrl, 
            loadKeyStore(keyStoreLocation), // load the InputStream
            keyStorePassword, 
            signingCertificateAlias, 
            signingCertificatePassword, 
            acquirerCertificateAlias, 
            acquirerUrl_DirectoryReq, 
            acquirerUrl_TransactionReq, 
            acquirerUrl_StatusReq, 
            logsEnabled, 
            serviceLogsEnabled, 
            serviceLogsLocation, 
            serviceLogsPattern, 
            loggerFactory);
    }
    
    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStore A Java keystore InputStream that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, InputStream keyStore,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, ILoggerFactory loggerFactory) throws IOException {

                this.eMandateContractId = eMandateContractId;
        this.eMandateContractSubId = eMandateContractSubId;
        this.merchantReturnUrl = merchantReturnUrl;
        this.signingCertificateAlias = signingCertificateAlias;
        this.signingCertificatePassword = signingCertificatePassword;
        this.acquirerCertificateAlias = acquirerCertificateAlias;
        this.acquirerUrl_DirectoryReq = acquirerUrl_DirectoryReq;
        this.acquirerUrl_TransactionReq = acquirerUrl_TransactionReq;
        this.acquirerUrl_StatusReq = acquirerUrl_StatusReq;
        this.logsEnabled = logsEnabled;
        this.serviceLogsEnabled = serviceLogsEnabled;
        this.serviceLogsLocation = serviceLogsLocation;
        this.serviceLogsPattern = serviceLogsPattern;
        this.tls12Enabled = false;
        this.loggerFactory = loggerFactory != null ? loggerFactory : new LoggerFactory();

        setKeyStoreAndPass(keyStore, keyStorePassword);
    }
    
    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStore A Java keystore InputStream that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param tls12Enabled This tells the library that should use tls 1.2.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, InputStream keyStore,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, boolean tls12Enabled, ILoggerFactory loggerFactory) throws IOException {

                this.eMandateContractId = eMandateContractId;
        this.eMandateContractSubId = eMandateContractSubId;
        this.merchantReturnUrl = merchantReturnUrl;
        this.signingCertificateAlias = signingCertificateAlias;
        this.signingCertificatePassword = signingCertificatePassword;
        this.acquirerCertificateAlias = acquirerCertificateAlias;
        this.acquirerUrl_DirectoryReq = acquirerUrl_DirectoryReq;
        this.acquirerUrl_TransactionReq = acquirerUrl_TransactionReq;
        this.acquirerUrl_StatusReq = acquirerUrl_StatusReq;
        this.logsEnabled = logsEnabled;
        this.serviceLogsEnabled = serviceLogsEnabled;
        this.serviceLogsLocation = serviceLogsLocation;
        this.serviceLogsPattern = serviceLogsPattern;
        this.tls12Enabled = tls12Enabled;
        this.loggerFactory = loggerFactory != null ? loggerFactory : new LoggerFactory();

        setKeyStoreAndPass(keyStore, keyStorePassword);
    }
    
    /**
     * Constructor that highlights all required fields for this object.
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     * @param keyStoreLocation A Java keystore (file on the disk) that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     * @param signingCertificatePassword The password of the private key of the signing certificate
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor
     * bank.
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     * @param logsEnabled This tells the library that it should output debug logging messages.
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not. Default is true.
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * @param tls12Enabled This tells the library that should use tls 1.2.
     * @param loggerFactory ILoggerFactory instance that is used to create ILogger object.
     */
    public Configuration(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl, String keyStoreLocation,
            String keyStorePassword, String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias,
            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
            String serviceLogsLocation, String serviceLogsPattern, boolean tls12Enabled, ILoggerFactory loggerFactory) throws IOException {
        
        this(
            eMandateContractId, 
            eMandateContractSubId, 
            merchantReturnUrl, 
            loadKeyStore(keyStoreLocation), // load the InputStream
            keyStorePassword, 
            signingCertificateAlias, 
            signingCertificatePassword, 
            acquirerCertificateAlias, 
            acquirerUrl_DirectoryReq, 
            acquirerUrl_TransactionReq, 
            acquirerUrl_StatusReq, 
            logsEnabled, 
            serviceLogsEnabled, 
            serviceLogsLocation, 
            serviceLogsPattern,
            tls12Enabled,
            loggerFactory);
    }
    
    /**
     * Clone a configuration object
     *
     * @return new Configuration object
     */
    public Configuration clone() {
        Configuration result = new Configuration();
        result.eMandateContractId = this.eMandateContractId;
        result.eMandateContractSubId = this.eMandateContractSubId;
        result.merchantReturnUrl = this.merchantReturnUrl;
        result.keyStoreLocation = this.keyStoreLocation;
        result.keyStorePassword = this.keyStorePassword;
        result.signingCertificateAlias = this.signingCertificateAlias;
        result.signingCertificatePassword = this.signingCertificatePassword;
        result.acquirerCertificateAlias = this.acquirerCertificateAlias;
        result.acquirerUrl_DirectoryReq = this.acquirerUrl_DirectoryReq;
        result.acquirerUrl_TransactionReq = this.acquirerUrl_TransactionReq;
        result.acquirerUrl_StatusReq = this.acquirerUrl_StatusReq;
        result.logsEnabled = this.logsEnabled;
        result.serviceLogsEnabled = this.serviceLogsEnabled;
        result.serviceLogsLocation = this.serviceLogsLocation;
        result.serviceLogsPattern = this.serviceLogsPattern;
        result.tls12Enabled = this.tls12Enabled;
        result.loggerFactory = this.loggerFactory;

        result.keyStore = this.keyStore;
        return result;
    }

    /**
     * Attempts to load the settings from the application's configuration
     * @param is an InputStream where the configuration should be loaded from (e.g. a file)
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void Load(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

        seteMandateContractId(getConfigValue(doc, "eMandates.Contract.Id"));
        String configValue = getConfigValue(doc, "eMandates.Contract.SubId");
        if (configValue != null) {
            seteMandateContractSubId(Integer.parseInt(configValue));
        }
        setMerchantReturnUrl(getConfigValue(doc, "eMandates.Merchant.ReturnUrl"));
        setSigningCertificateAlias(getConfigValue(doc, "eMandates.SigningCertificate.Alias"));
        setSigningCertificatePassword(getConfigValue(doc, "eMandates.SigningCertificate.Password"));
        setAcquirerCertificateAlias(getConfigValue(doc, "eMandates.AcquirerCertificate.Alias"));
        setAcquirerAlternateCertificateAlias(getConfigValue(doc, "eMandates.AcquirerAlternateCertificate.Alias"));
        setAcquirerUrl_DirectoryReq(getConfigValue(doc, "eMandates.Acquirer.DirectoryRequestUrl"));
        setAcquirerUrl_TransactionReq(getConfigValue(doc, "eMandates.Acquirer.TransactionRequestUrl"));
        setAcquirerUrl_StatusReq(getConfigValue(doc, "eMandates.Acquirer.StatusRequestUrl"));
        setLogsEnabled(Boolean.parseBoolean(getConfigValue(doc, "eMandates.Logs.Enabled")));
        setServiceLogsEnabled(Boolean.parseBoolean(getConfigValue(doc, "eMandates.ServiceLogs.Enabled")));
        setServiceLogsLocation(getConfigValue(doc, "eMandates.ServiceLogs.Location"));
        setServiceLogsPattern(getConfigValue(doc, "eMandates.ServiceLogs.Pattern"));
        setTls12Enabled(Boolean.parseBoolean(getConfigValue(doc, "eMandates.TLS12.Enabled")));
        setLoggerFactory(new LoggerFactory());

        setKeyStoreLocationAndPass(
                getConfigValue(doc, "eMandates.KeyStore.Location"),
                getConfigValue(doc, "eMandates.KeyStore.Password"));
    }

    /**
     * Sets the Configuration object to be used by Communicator instances
     * @param values the Configuration instance to get values from
     * @throws IOException
     */
    public void Setup(Configuration values) throws IOException {
        seteMandateContractId(values.geteMandateContractId());
        seteMandateContractSubId(values.geteMandateContractSubId());
        setMerchantReturnUrl(values.getMerchantReturnUrl());
        setSigningCertificateAlias(values.getSigningCertificateAlias());
        setSigningCertificatePassword(values.getSigningCertificatePassword());
        setAcquirerCertificateAlias(values.getAcquirerCertificateAlias());
        setAcquirerAlternateCertificateAlias(values.getAcquirerAlternateCertificateAlias());
        setAcquirerUrl_DirectoryReq(values.getAcquirerUrl_DirectoryReq());
        setAcquirerUrl_TransactionReq(values.getAcquirerUrl_TransactionReq());
        setAcquirerUrl_StatusReq(values.getAcquirerUrl_StatusReq());
        setLogsEnabled(values.isLogsEnabled());
        setServiceLogsEnabled(values.isServiceLogsEnabled());
        setServiceLogsLocation(values.getServiceLogsLocation());
        setServiceLogsPattern(values.getServiceLogsPattern());
        setTls12Enabled(values.isTls12Enabled());
        setLoggerFactory(values.getLoggerFactory());

        setKeyStoreLocationAndPass(
                values.getKeyStoreLocation(),
                values.getKeyStorePassword());
    }

    /**
     * @return eMandate.ContractID as supplied to you by the creditor bank. If the eMandate.ContractID has less than 9 digits, use leading
     * zeros to fill out the field.
     */
    public String geteMandateContractId() {
        return eMandateContractId;
    }

    /**
     * @param eMandateContractId eMandate.ContractID as supplied to you by the creditor bank. If the eMandate.ContractID has less than 9 digits, use leading
     * zeros to fill out the field.
     */
    public void seteMandateContractId(String eMandateContractId) {
        this.eMandateContractId = eMandateContractId;
    }

    /**
     * @return eMandate.ContractSubId as supplied to you by the creditor bank. If you do not have a ContractSubId, use 0 for this field.
     */
    public int geteMandateContractSubId() {
        return eMandateContractSubId;
    }

    /**
     * @param eMandateContractSubId eMandate.ContractSubId as supplied to you by the creditor bank. If you do not have a ContractSubId,
     * use 0 for this field.
     */
    public void seteMandateContractSubId(int eMandateContractSubId) {
        this.eMandateContractSubId = eMandateContractSubId;
    }

    /**
     * @return A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     */
    public String getMerchantReturnUrl() {
        return merchantReturnUrl;
    }

    /**
     * @param merchantReturnUrl A valid URL to which the debtor banks redirects to, after the debtor has authorized a transaction.
     */
    public void setMerchantReturnUrl(String merchantReturnUrl) {
        this.merchantReturnUrl = merchantReturnUrl;
    }

    /**
     * @return A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     */
    public String getSigningCertificateAlias() {
        return signingCertificateAlias;
    }

    /**
     * @param signingCertificateAlias A string which specifies the alias of the certificate to use to sign messages to the creditor bank.
     */
    public void setSigningCertificateAlias(String signingCertificateAlias) {
        this.signingCertificateAlias = signingCertificateAlias;
    }

    /**
     * @return A string which specifies the alias of the certificate to use to validate messages from the creditor bank
     */
    public String getAcquirerCertificateAlias() {
        return acquirerCertificateAlias;
    }

    /**
     * @param acquirerCertificateAlias A string which specifies the alias of the certificate to use to validate messages from the creditor bank
     */
    public void setAcquirerCertificateAlias(String acquirerCertificateAlias) {
        this.acquirerCertificateAlias = acquirerCertificateAlias;
    }
    
    /**
     * @param A string which specifies the alias of the alternate certificate to validate  received messages from the creditor bank
     */
    public String getAcquirerAlternateCertificateAlias() {
		return acquirerAlternateCertificateAlias;
	}
    
    /**
     * @param acquirerAlternateCertificateAlias A string which specifies the alias of the alternate certificate to validate received messages from the creditor bank
     */
	public void setAcquirerAlternateCertificateAlias(String acquirerAlternateCertificateAlias) {
		this.acquirerAlternateCertificateAlias = acquirerAlternateCertificateAlias;
	}

    /**
     * @return The URL to which the library sends Directory request messages
     */
    public String getAcquirerUrl_DirectoryReq() {
        return acquirerUrl_DirectoryReq;
    }

    /**
     * @param acquirerUrl_DirectoryReq The URL to which the library sends Directory request messages
     */
    public void setAcquirerUrl_DirectoryReq(String acquirerUrl_DirectoryReq) {
        this.acquirerUrl_DirectoryReq = acquirerUrl_DirectoryReq;
    }

    /**
     * @return The URL to which the library sends Transaction messages (including eMandates messages).
     */
    public String getAcquirerUrl_TransactionReq() {
        return acquirerUrl_TransactionReq;
    }

    /**
     * @param acquirerUrl_TransactionReq The URL to which the library sends Transaction messages (including eMandates messages).
     */
    public void setAcquirerUrl_TransactionReq(String acquirerUrl_TransactionReq) {
        this.acquirerUrl_TransactionReq = acquirerUrl_TransactionReq;
    }

    /**
     * @return The URL to which the library sends Status request messages.
     */
    public String getAcquirerUrl_StatusReq() {
        return acquirerUrl_StatusReq;
    }

    /**
     * @param acquirerUrl_StatusReq The URL to which the library sends Status request messages.
     */
    public void setAcquirerUrl_StatusReq(String acquirerUrl_StatusReq) {
        this.acquirerUrl_StatusReq = acquirerUrl_StatusReq;
    }

    /**
     * @return The password of the private key of the signing certificate
     */
    public String getSigningCertificatePassword() {
        return signingCertificatePassword;
    }

    /**
     * @param signingCertificatePassword The password of the private key of the signing certificate
     */
    public void setSigningCertificatePassword(String signingCertificatePassword) {
        this.signingCertificatePassword = signingCertificatePassword;
    }

    /**
     * @return The password used to access the keystore
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * @return A Java keystore (file on the disk) that stores the certificates
     */
    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    /**
     * @param keyStoreLocation A Java keystore (file on the disk) that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     */
    public void setKeyStoreLocationAndPass(String keyStoreLocation, String keyStorePassword) throws IOException {
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePassword = keyStorePassword;

        this.keyStore = loadKeyStore(getKeyStoreLocation());
    }

    /**
     * @param keyStore A Java InputStream keystore that stores the certificates
     * @param keyStorePassword The password used to access the keystore
     */
    public void setKeyStoreAndPass(InputStream keyStore, String keyStorePassword) throws IOException {
        this.keyStorePassword = keyStorePassword;

        this.keyStore = markSupported(keyStore);
        this.keyStore.mark(Integer.MAX_VALUE);
    }

    /**
     * @return the keyStore
     */
    InputStream getKeyStore() {
        return keyStore;
    }

    /**
     * @return This tells the library that it should output debug logging messages.
     */
    public boolean isLogsEnabled() {
        return logsEnabled;
    }

    /**
     * @param logsEnabled This tells the library that it should output debug logging messages.
     */
    public void setLogsEnabled(boolean logsEnabled) {
        this.logsEnabled = logsEnabled;
    }

    /**
     * @return This tells the library that it should save ISO pain raw messages or not. Default is true.
     */
    public boolean isServiceLogsEnabled() {
        return serviceLogsEnabled;
    }

    /**
     * @param serviceLogsEnabled This tells the library that it should save ISO pain raw messages or not.
     */
    public void setServiceLogsEnabled(boolean serviceLogsEnabled) {
        this.serviceLogsEnabled = serviceLogsEnabled;
    }

    /**
     * @return A directory on the disk where the library saves ISO pain raw messages.
     */
    public String getServiceLogsLocation() {
        return serviceLogsLocation;
    }

    /**
     * @param serviceLogsLocation A directory on the disk where the library saves ISO pain raw messages.
     */
    public void setServiceLogsLocation(String serviceLogsLocation) {
        this.serviceLogsLocation = serviceLogsLocation;
    }

    /**
     * @return A string that describes a pattern to distinguish the ISO pain raw messages.
     * For example, %Y-%M-%D\%h%m%s.%f-%a.xml -> 102045.924-AcquirerTrxReq.xml
     * Other remarks:
     * %Y = current year
     * %M = current month
     * %D = current day
     * %h = current hour
     * %m = current minute
     * %s = current second
     * %f = current millisecond
     * %a = current action
     */
    public String getServiceLogsPattern() {
        return serviceLogsPattern;
    }

    /**
     * @param serviceLogsPattern A string that describes a pattern to distinguish the ISO pain raw messages.
     * For example, %Y-%M-%D\%h%m%s.%f-%a.xml -> 102045.924-AcquirerTrxReq.xml
     * Other remarks:
     * %Y = current year
     * %M = current month
     * %D = current day
     * %h = current hour
     * %m = current minute
     * %s = current second
     * %f = current millisecond
     * %a = current action
     */
    public void setServiceLogsPattern(String serviceLogsPattern) {
        this.serviceLogsPattern = serviceLogsPattern;
    }

    /**
     * @return ILoggerFactory instance that is used to create ILogger object
     */
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    /**
     * @param loggerFactory the loggerFactory to set
     */
    public void setLoggerFactory(ILoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }
    
    /**
     * 
     * @param tls12Enabled flag that indicates the library to use TLS 1.2
     */
    public void setTls12Enabled(boolean tls12Enabled) {
        this.tls12Enabled = tls12Enabled;
    }
    
    /**
     * 
     * @return the tls12Enabled
     */
    public boolean isTls12Enabled() {
        return tls12Enabled;
    }
    
//    private void setParamsWithoutKeyStore(String eMandateContractId, int eMandateContractSubId, String merchantReturnUrl,
//        String signingCertificateAlias, String signingCertificatePassword, String acquirerCertificateAlias,
//            String acquirerUrl_DirectoryReq, String acquirerUrl_TransactionReq, String acquirerUrl_StatusReq, boolean logsEnabled, boolean serviceLogsEnabled,
//            String serviceLogsLocation, String serviceLogsPattern, ILoggerFactory loggerFactory){
//        this.eMandateContractId = eMandateContractId;
//        this.eMandateContractSubId = eMandateContractSubId;
//        this.merchantReturnUrl = merchantReturnUrl;
//        this.signingCertificateAlias = signingCertificateAlias;
//        this.signingCertificatePassword = signingCertificatePassword;
//        this.acquirerCertificateAlias = acquirerCertificateAlias;
//        this.acquirerUrl_DirectoryReq = acquirerUrl_DirectoryReq;
//        this.acquirerUrl_TransactionReq = acquirerUrl_TransactionReq;
//        this.acquirerUrl_StatusReq = acquirerUrl_StatusReq;
//        this.logsEnabled = logsEnabled;
//        this.serviceLogsEnabled = serviceLogsEnabled;
//        this.serviceLogsLocation = serviceLogsLocation;
//        this.serviceLogsPattern = serviceLogsPattern;
//        this.loggerFactory = loggerFactory != null ? loggerFactory : new LoggerFactory();
//    }
    
    private static InputStream loadKeyStore(String storeLocation) throws IOException {
        URL url = ClassLoader.getSystemClassLoader().getResource(storeLocation);
        if (url == null) {
            url = Configuration.class.getClassLoader().getResource(storeLocation);
        }
		InputStream keyStore = markSupported(url.openStream());
		keyStore.mark(Integer.MAX_VALUE);
		return keyStore;
	}

	private static InputStream markSupported(InputStream in) {
		if (in.markSupported()) {
			return in;
		}
		return new BufferedInputStream(in);
	}
          
    private String getConfigValue(Document doc, String key) {
        NodeList nl = doc.getElementsByTagName("add");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if (e.getAttribute("key").equalsIgnoreCase(key)) {
                return e.getAttribute("value");
            }
        }
        return null;
    }
}
