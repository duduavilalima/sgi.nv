package br.com.araujoabreu.sgi.webservices.soawebservices;

import br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.Endereco;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.PessoaFisicaNFe;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.CNAE;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.PessoaJuridicaNFe;

import java.rmi.RemoteException;

public class CDCSoapStub extends org.apache.axis.client.Stub implements CDCSoap {
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

	public CDCSoapStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public CDCSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public CDCSoapStub(javax.xml.rpc.Service service) {
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
		Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("SOAWebServices", "ArrayOfEmail");
		cachedSerQNames.add(qName);
		cls = Email[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("SOAWebServices", "Email");
		qName2 = new javax.xml.namespace.QName("SOAWebServices", "Email");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("SOAWebServices", "ArrayOfEndereco");
		cachedSerQNames.add(qName);
		cls = Endereco[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe");
		qName2 = new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("SOAWebServices", "ArrayOfTelefone");
		cachedSerQNames.add(qName);
		cls = Telefone[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("SOAWebServices", "Telefone");
		qName2 = new javax.xml.namespace.QName("SOAWebServices", "Telefone");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("SOAWebServices", "Credenciais");
		cachedSerQNames.add(qName);
		cls = Credenciais.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Email");
		cachedSerQNames.add(qName);
		cls = Email.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe");
		cachedSerQNames.add(qName);
		cls = Endereco.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "GeoLocalizacao");
		cachedSerQNames.add(qName);
		cls = GeoLocalizacao.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaEstendida");
		cachedSerQNames.add(qName);
		cls = PessoaFisicaEstendida.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaSimplificada");
		cachedSerQNames.add(qName);
		cls = PessoaFisicaSimplificada.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaSimplificada");
		cachedSerQNames.add(qName);
		cls = PessoaJuridicaSimplificada.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Serialization");
		cachedSerQNames.add(qName);
		cls = Serialization.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "Telefone");
		cachedSerQNames.add(qName);
		cls = Telefone.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "TiposEnderecos");
		cachedSerQNames.add(qName);
		cls = TiposEnderecos.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("SOAWebServices", "TiposTelefones");
		cachedSerQNames.add(qName);
		cls = TiposTelefones.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "PessoaFisicaNFe");
		cachedSerQNames.add(qName);
		cls = PessoaFisicaNFe.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "Serialization");
		cachedSerQNames.add(qName);
		cls = Serialization.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "ArrayOfCNAE");
		cachedSerQNames.add(qName);
		cls = CNAE[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE");
		qName2 = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "ArrayOfEndereco");
		cachedSerQNames.add(qName);
		cls = Endereco[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "EnderecoNfe");
		qName2 = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "EnderecoNfe");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE");
		cachedSerQNames.add(qName);
		cls = CNAE.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "EnderecoNfe");
		cachedSerQNames.add(qName);
		cls = Endereco.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "GeoLocalizacao");
		cachedSerQNames.add(qName);
		cls = GeoLocalizacao.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "PessoaJuridicaNFe");
		cachedSerQNames.add(qName);
		cls = PessoaJuridicaNFe.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Serialization");
		cachedSerQNames.add(qName);
		cls = Serialization.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "TiposEnderecos");
		cachedSerQNames.add(qName);
		cls = TiposEnderecos.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("PessoaFisicaEstendida");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			Credenciais.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaEstendida"));
		oper.setReturnClass(PessoaFisicaEstendida.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaEstendidaResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("PessoaFisicaSimplificada");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			Credenciais.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaSimplificada"));
		oper.setReturnClass(PessoaFisicaSimplificada.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaSimplificadaResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("PessoaJuridicaSimplificada");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			Credenciais.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaSimplificada"));
		oper.setReturnClass(PessoaJuridicaSimplificada.class);
		oper.setReturnQName(new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaSimplificadaResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("PessoaJuridicaNFe");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			Credenciais.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "PessoaJuridicaNFe"));
		oper.setReturnClass(PessoaJuridicaNFe.class);
		oper.setReturnQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "PessoaJuridicaNFeResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("PessoaFisicaNFe");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("SOAWebServices", "Credenciais"),
			Credenciais.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("SOAWebServices", "Documento"),
			org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"),
			String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "PessoaFisicaNFe"));
		oper.setReturnClass(PessoaFisicaNFe.class);
		oper.setReturnQName(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "PessoaFisicaNFeResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[4] = oper;

	}

	@SuppressWarnings("rawtypes")
	protected org.apache.axis.client.Call createCall() throws RemoteException {
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
	public PessoaFisicaEstendida pessoaFisicaEstendida(
		Credenciais credenciais, String documento) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/PessoaFisicaEstendida");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaEstendida"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof RemoteException) {
			throw (RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (PessoaFisicaEstendida) _resp;
			} catch (Exception _exception) {
				return (PessoaFisicaEstendida) org.apache.axis.utils.JavaUtils.convert(_resp,
					PessoaFisicaEstendida.class);
			}
		}
	}

	@Override
	public PessoaFisicaSimplificada pessoaFisicaSimplificada(
		Credenciais credenciais, String documento) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/PessoaFisicaSimplificada");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaSimplificada"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof RemoteException) {
			throw (RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (PessoaFisicaSimplificada) _resp;
			} catch (Exception _exception) {
				return (PessoaFisicaSimplificada) org.apache.axis.utils.JavaUtils.convert(_resp,
					PessoaFisicaSimplificada.class);
			}
		}
	}

	@Override
	public PessoaJuridicaSimplificada pessoaJuridicaSimplificada(
		Credenciais credenciais, String documento) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/PessoaJuridicaSimplificada");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaSimplificada"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof RemoteException) {
			throw (RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (PessoaJuridicaSimplificada) _resp;
			} catch (Exception _exception) {
				return (PessoaJuridicaSimplificada) org.apache.axis.utils.JavaUtils.convert(_resp,
					PessoaJuridicaSimplificada.class);
			}
		}
	}

	@Override
	public PessoaJuridicaNFe pessoaJuridicaNFe(
		Credenciais credenciais, String documento) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/PessoaJuridicaNFe");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaNFe"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof RemoteException) {
			throw (RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (PessoaJuridicaNFe) _resp;
			} catch (Exception _exception) {
				return (PessoaJuridicaNFe) org.apache.axis.utils.JavaUtils.convert(
					_resp, PessoaJuridicaNFe.class);
			}
		}
	}

	@Override
	public PessoaFisicaNFe pessoaFisicaNFe(
		Credenciais credenciais, String documento) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("SOAWebServices/PessoaFisicaNFe");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaNFe"));

		setRequestHeaders(_call);
		setAttachments(_call);
		Object _resp = _call.invoke(new Object[]{credenciais, documento});

		if (_resp instanceof RemoteException) {
			throw (RemoteException) _resp;
		} else {
			extractAttachments(_call);
			try {
				return (PessoaFisicaNFe) _resp;
			} catch (Exception _exception) {
				return (PessoaFisicaNFe) org.apache.axis.utils.JavaUtils.convert(_resp,
					PessoaFisicaNFe.class);
			}
		}
	}

	@Override
	public PessoaJuridicaNFe pessoaJuridicaNFe(Credenciais credenciais) throws RemoteException {
		return pessoaJuridicaNFe(credenciais, credenciais.getDocumento());
	}

}
