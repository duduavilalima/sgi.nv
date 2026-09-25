package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class AdministracaoSoapStub extends org.apache.axis.client.Stub implements AdministracaoSoap {
	static final org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[5];
		_initOperationDesc1();
	}

	@SuppressWarnings("rawtypes")
	private final java.util.Vector cachedSerClasses = new java.util.Vector();
	@SuppressWarnings("rawtypes")
	private final java.util.Vector cachedSerQNames = new java.util.Vector();
	@SuppressWarnings("rawtypes")
	private final java.util.Vector cachedSerFactories = new java.util.Vector();
	@SuppressWarnings("rawtypes")
	private final java.util.Vector cachedDeserFactories = new java.util.Vector();

	public AdministracaoSoapStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public AdministracaoSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public AdministracaoSoapStub(javax.xml.rpc.Service service) {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("SOAWebServices", "ArrayOfHistorico");
		cachedSerQNames.add(qName);
		cls = HistoricoSOAWeb[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("SOAWebServices", "Historico");
		qName2 = new javax.xml.namespace.QName("SOAWebServices", "Historico");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("SOAWebServices", "ArrayOfProduto");
		cachedSerQNames.add(qName);
		cls = ProdutoSOAWeb[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("SOAWebServices", "Produto");
		qName2 = new javax.xml.namespace.QName("SOAWebServices", "Produto");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("SOAWebServices", "Credenciais");
		cachedSerQNames.add(qName);
		cls = CredenciaisSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Extrato");
		cachedSerQNames.add(qName);
		cls = ExtratoSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnalitico");
		cachedSerQNames.add(qName);
		cls = ExtratoAnalitico.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Historico");
		cachedSerQNames.add(qName);
		cls = HistoricoSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "HistoricoDetalhado");
		cachedSerQNames.add(qName);
		cls = HistoricoDetalhadoSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Produto");
		cachedSerQNames.add(qName);
		cls = ProdutoSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Saldo");
		cachedSerQNames.add(qName);
		cls = SaldoSOAWeb.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Serialization");
		cachedSerQNames.add(qName);
		cls = Serialization.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("Saldo");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"), CredenciaisSOAWeb.class, false,
			false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "Saldo"));
		oper.setReturnClass(SaldoSOAWeb.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "SaldoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("Extrato");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"), CredenciaisSOAWeb.class, false,
			false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Mes"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Ano"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "Extrato"));
		oper.setReturnClass(ExtratoSOAWeb.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "ExtratoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("ExtratoAnalitico");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"), CredenciaisSOAWeb.class, false,
			false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Mes"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Ano"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnalitico"));
		oper.setReturnClass(ExtratoAnalitico.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnaliticoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("ExtratoAnaliticoPorDocumento");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"), CredenciaisSOAWeb.class, false,
			false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnalitico"));
		oper.setReturnClass(ExtratoAnalitico.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnaliticoPorDocumentoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("HistoricoDetalhado");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"), CredenciaisSOAWeb.class, false,
			false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "UniqueID"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "HistoricoDetalhado"));
		oper.setReturnClass(HistoricoDetalhadoSOAWeb.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "HistoricoDetalhadoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[4] = oper;

	}

	@SuppressWarnings("rawtypes")
	protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames.get(i);
						Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) cachedSerFactories.get(i);
							Class df = (Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	@Override
	public SaldoSOAWeb saldo(CredenciaisSOAWeb credenciais) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/Saldo");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "Saldo"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{credenciais});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (SaldoSOAWeb) _resp;
				} catch (Exception _exception) {
					return (SaldoSOAWeb) org.apache.axis.utils.JavaUtils.convert(_resp, SaldoSOAWeb.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			axisFaultException.printStackTrace();
			throw axisFaultException;
		}
	}

	@Override
	public ExtratoSOAWeb extrato(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/Extrato");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "Extrato"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, mes, ano});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (ExtratoSOAWeb) _resp;
			} catch (Exception _exception) {
				return (ExtratoSOAWeb) org.apache.axis.utils.JavaUtils.convert(_resp, ExtratoSOAWeb.class);
			}
		}
	}

	@Override
	public ExtratoAnalitico extratoAnalitico(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/ExtratoAnalitico");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnalitico"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, mes, ano});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (ExtratoAnalitico) _resp;
			} catch (Exception _exception) {
				return (ExtratoAnalitico) org.apache.axis.utils.JavaUtils.convert(_resp, ExtratoAnalitico.class);
			}
		}
	}

	@Override
	public ExtratoAnalitico extratoAnaliticoPorDocumento(CredenciaisSOAWeb credenciais, String documento) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/ExtratoAnaliticoPorDocumento");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnaliticoPorDocumento"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (ExtratoAnalitico) _resp;
			} catch (Exception _exception) {
				return (ExtratoAnalitico) org.apache.axis.utils.JavaUtils.convert(_resp, ExtratoAnalitico.class);
			}
		}
	}

	@Override
	public HistoricoDetalhadoSOAWeb historicoDetalhado(CredenciaisSOAWeb credenciais, String uniqueID) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/HistoricoDetalhado");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "HistoricoDetalhado"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, uniqueID});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (HistoricoDetalhadoSOAWeb) _resp;
			} catch (Exception _exception) {
				return (HistoricoDetalhadoSOAWeb) org.apache.axis.utils.JavaUtils.convert(_resp, HistoricoDetalhadoSOAWeb.class);
			}
		}
	}

}
