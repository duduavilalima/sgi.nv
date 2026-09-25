angular.module('services', [])

    /* PESSOA */
    .factory('Pessoa', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/pessoa/:id/:action', null, {
            copy: {
                method: 'POST',
                params: {
                    'action': 'copiar'
                }
            },
            buscarNomeFantasiaDocumento: {
                method: 'POST',
                params: {
                    'action': 'buscarNomeFantasiaDocumento'
                },
                isArray: true
            },
        });
    }])

    /* ENDEREÇO */
    .factory('Endereco', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/endereco/:pre_action/:id/:action', null, {
            qbePessoa: {
                method: 'POST',
                params: {
                    'pre_action': 'busca',
                    'action': 'pessoa'
                },
            },
            buscarTipoLogradouro: {
                method: 'POST',
                params: {
                    'action': 'buscartipologradouro'
                },
                isArray: true
            }
        });
    }])

    /* CONTRATO CIDADE FATURAMENTO */
    .factory('ContratoCidadePrestacaoServicos', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/contrato/cidade/faturamento/:id/:action', null, {
            buscarTodos: {
                method: 'POST',
                params: {
                    'action': 'buscarTodos'
                },
            }
        });
    }])

    /* CIDADE */
    .factory('Cidade', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/cidade/:id/:action', null, {
            buscarListaPorNome: {
                method: 'POST',
                params: {
                    'action': 'buscarcidadespornome'
                },
                isArray: true
            },
            buscarUFs: {
                method: 'GET',
                params: {
                    'action': 'buscarUFs'
                },
                isArray: true
            },
			getCidadesISS: {
				method: 'GET',
				url: $restServer + 'rest/cidade/buscarcidadesiss/:uf',
				isArray: true
			}
        });
    }])


    /* TRIBUTO */
    .factory('Tributo', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/tributo/:id/:action', null, {
            buscarTributoByExibeNota: {
                method: 'GET',
                params: {
                    'action': 'buscarTributoByExibeNota'
                },
                isArray: true
            }
        });
    }])

    /* CATEGORIA TRIBUTO */
    .factory('CategoriaTributo', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/categoriatributo/:id/:action', null, {
            getCategoriaTributoByCod: {
                method: 'GET',
                url: $restServer + 'rest/categoriatributo/getCategoriaTributoByCod/:id'
            },
            getListaCategoriaTributoByCodTributo: {
                method: 'GET',
                url: $restServer + 'rest/categoriatributo/getListaCategoriaTributoByCodTributo/:id',
                isArray: true
            },
            getListaCategoriaTributoByTributoUF: {
                method: 'GET',
                url: $restServer + 'rest/categoriatributo/getListaCategoriaTributoByTributoUF/:codTributo/:uf',
                isArray: true
            }
        });
    }])

    /* ALIQUOTA */
    .factory('Aliquota', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/aliquota/:id/:action', null, {
        	getListaAliquotaByCodCategoriaTributo: {
				method: 'GET',
				url: $restServer + 'rest/aliquota/getListaAliquotaByCodCategoriaTributo/:id',
				isArray: true
			}

        });
    }])


    /* TIPO COBRANÇA FATURAMENTO */
    .factory('TipoCobrancaFaturamento', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/tipocobrancafaturamento/:id/:action', null, {});
    }])


    /* CONTATO */
    .factory('Contato', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/contato/:id/:action', null, {});
    }])

    /* UNIDADE */
    .factory('Unidade', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/unidade/:id/:action', null, {
            unidadesporempresa: {
                method: 'GET',
                params: {
                    'action': 'unidadesporempresa'
                },
                isArray: true
            },
            unidadesporempresaexceto: {
                method: 'POST',
                params: {
                    'action': 'unidadesporempresaexceto'
                },
                isArray: true
            },
            unidadesfaturamento: {
                method: 'GET',
                params: {
                    'action': 'unidadesfaturamento'
                },
                isArray: true
            },
            unidadesmatrizativas: {
                method: 'GET',
                params: {
                    'action': 'unidadesmatrizativas'
                },
                isArray: true
            }

        });
    }])


    /* MOTIVO RETORNO */
    .factory('MotivoRetorno', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/:id/:action', null, {
            buscarMotivoRetornoByChave: {
                method: 'GET',
                url: $restServer + 'rest/motivo/retorno/buscarMotivoRetornoByChave/:chave',
                isArray: true
            }
        });
    }])

    /* MOTIVO RETORNO DOCUMENTO */
    .factory('MotivoRetornoDocumento', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/documento/:id/:action', null, {});
    }])

    /* MOTIVO RETORNO FATURAMENTO */
    .factory('MotivoRetornoFaturamento', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/faturamento/:id/:action', null, {});
    }])

    /* MOTIVO RETORNO ETAPA */
    .factory('MotivoRetornoEtapa', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/faturamento/etapa/:id/:action', null, {});
    }])

    /* MOTIVO RETORNO CHEQUE CONTA CORRENTE */
    .factory('MotivoRetornoChequeContaCorrente', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/chequecontacorrente/:id/:action', null, {});
    }])

    /* MOTIVO RETORNO FATURAMENTO */
    .factory('MotivoRetornoNota', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/nota/:id/:action', null, {
            get: {
                method: 'GET'
            },
            update: {
                method: 'PUT'
            },
            create: {
                method: 'POST',
                url: $restServer + 'rest/motivo/retorno/nota/create'
            },
            qbe: {
                method: 'POST',
                url: $restServer + 'rest/motivo/retorno/nota/busca'
            },
            saveCancelamentoNotaFiscal: {
                method: 'POST',
                url: $restServer + 'rest/motivo/retorno/nota/cancelarNotaFiscal'
            }
        });
    }])

    /* MOTIVO RETORNO PARCELA */
    .factory('MotivoRetornoParcela', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/notasaidaparcela/:id/:action', null, {
            recebimento: {
                method: 'POST',
                params: {
                    'action': 'recebimento'
                }
            }
        });
    }])

    /* MOTIVO APLICACAO */
    .factory('MotivoAplicacao', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/aplicacao/:id/:action', null, {
            buscarByChave: {
                method: 'GET',
                url: $restServer + 'rest/motivo/aplicacao/buscarByChave/:chave',
            },
            findByCodMotivoRetornoDoc: {
				method: 'GET',
				url: $restServer + 'rest/motivo/aplicacao/buscarByCodMotivoRetornoDocumento/:id'
			}
        });
    }])

    /* MOTIVO RETORNO CENTRO FINANCEIRO */
    .factory('MotivoRetornoCentroFinanceiro', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/retorno/centrofinanceiro/:id/:action', null, {});
    }])

    /* MOTIVO DOCUMENTO */
    .factory('MotivoDocumento', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/documento/:id/:action', null, {});
    }])

    /* NOTA SAIDA PARCELA */
    .factory('NotaSaidaParcela', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/notafiscalfaturamento/parcela/:id/:action/:codigoNota', null, {
            listarPorCodNotaSaida: {
                method: 'GET',
                params: {
                    'action': 'listarPorCodNotaSaida'
                },
                isArray: true
            },
            verificarLoteLoteContabilDiario: {
                method: 'GET',
                url: $restServer + 'rest/notafiscalfaturamento/parcela/verificarLoteLoteContabilDiario/:id'
            }
        });
    }])

    /* PARAMENTO */
    .factory('Parametro', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/parametro/:id/:action', null, {});
    }])


    /* RELATORIOS */
    .factory('Relatorios', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/relatorios/:id/:action', null, {
            buscarGerencias: {
                method: 'POST',
                params: {
                    'action': 'buscargerenciasporunidade'
                },
                isArray: true
            },
            buscarCentros: {
                method: 'POST',
                params: {
                    'action': 'buscarcentrosfinanceirosporgerencia'
                },
                isArray: true
            }
        });
    }])

    /* NOTA TRIBUTO ALIQUOTA */
    .factory('NotaTributoAliquota', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/notatributoaliquota/:id/:action', null, {
            listarPorNota: {
                method: 'GET',
                url: $restServer + 'rest/notatributoaliquota/listarPorNota/:codigoNota',
                isArray: true
            }
        });
    }])

    /* NOTA TRIBUTO ALIQUOTA EMISSÃO */
    .factory('NotaTributoAliquotaEmissao', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/notatributoaliquotaemissao/:id/:action', null, {
            listarPorNota: {
                method: 'GET',
                url: $restServer + 'rest/notatributoaliquotaemissao/listarPorNota/:codigoNota',
                isArray: true
            }
        });
    }])

    /* CONTA CONTABIL */
    .factory('ContaContabil', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/contacontabil/:id/:action', null, {
            buscapornomeounumero: {
                method: 'POST',
                params: {
                    'action': 'buscapornomeounumero'
                },
                isArray: true
            },
            buscapornomeounumeroeporempresa: {
                method: 'POST',
                params: {
                    'action': 'buscapornomeounumeroeporempresa'
                },
                isArray: true
            }
        });
    }])

    /* DADOS BANCÁRIOS */
    .factory('DadosBancarios', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/dadosbancarios/:id/:action', null, {
            todos: {
                method: 'GET',
                params: {
                    'action': 'todos'
                },
                isArray: true
            },
            homologarDadosBancariosManual: {
                method: 'POST',
                params: {
                    'action': 'homologardadosbancariosmanual'
                }
            },
            popup: {
                method: 'GET',
                url: $restServer + 'rest/dadosbancarios/popup/:id',
                isArray: true
            }
        });
    }])


    /* BOLETO */
    .factory('Boleto', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/boleto/:action/:tipoDocumentoPagamento/:numero', null, {
            linhadigitavel: {
                method: 'GET',
                params: {
                    'action': 'linhadigitavel'
                }
            },
            codigobarras: {
                method: 'GET',
                params: {
                    'action': 'codigobarras'
                }
            }
        });
    }])

    /* DIRETORIA */
    .factory('Diretoria', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/diretoria/:id/:action', null, {
            buscarDiretoria: {
                method: 'POST',
                params: {
                    'action': 'buscarDiretoria'
                },
                isArray: true
            },
            buscarDiretoriasDeContratos: {
                method: 'POST',
                params: {
                    'action': 'buscarDiretoriasDeContrato'
                },
                isArray: true
            },
            buscarDiretoriaPorEmpresa: {
                method: 'GET',
                params: {
                    'action': 'buscarDiretoriaPorEmpresa'
                },
                isArray: true
            }
        });
    }])

    /* TELEFONE */
    .factory('Telefone', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/telefone/:id/:action', null, {
            buscarOperadora: {
                method: 'GET',
                params: {
                    'action': 'buscaroperadora'
                },
                isArray: true
            },
            buscarListaTelefone: {
                method: 'POST',
                params: {
                    'action': 'buscarListaTelefone'
                },
                isArray: true
            }
        });
    }])

    /* FECHO */
    .factory('Fecho', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/fecho/:id/:action', null, {
            fechoByCodCentroFinanceiro: {
                method: 'GET',
                url: $restServer + 'rest/fecho/fechoByCodCentroFinanceiro/:id'
            },
            listaFechoByCodCentroFinanceiro: {
                method: 'GET',
                url: $restServer + 'rest/fecho/listaFechoByCodCentroFinanceiro/:id',
                isArray: true
            },
            addReajuste: {
                method: 'POST',
                params: {
                    'action': 'addReajuste'
                }
            },
            removerReajusteByCodCentroFinanceiro: {
                method: 'GET',
                url: $restServer + 'rest/fecho/removerReajusteByCodCentroFinanceiro/:id'
            }
        });
    }])

    /* FECHO MAO DE OBRA */
    .factory('FechoMaoDeObra', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/fechomaodeobra/:id/:action', null, {
            maoDeObraByCodFecho: {
                method: 'GET',
                url: $restServer + 'rest/fechomaodeobra/maoDeObraByCodFecho/:id',
                isArray: true
            }
        });
    }])

    /* FECHO MAO DE OBRA ADICIONAL */
    .factory('FechoMaoDeObraAdicional', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/fechomaodeobraadicional/:id/:action', null, {
            adicionalByCodMaoDeObra: {
                method: 'GET',
                params: {
                    'action': 'adicionalByCodMaoDeObra/:id/:action'
                },
                isArray: true
            }
        });
    }])


    /* FECHO DESPESA GERAL */
    .factory('FechoDespesaGeral', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/fechodespesageral/:id/:action', null, {
            despesaByCodFecho: {
                method: 'GET',
                url: $restServer + 'rest/fechodespesageral/despesaByCodFecho/:id',
                isArray: true
            }
        });
    }])

    /* ADITIVO */
    .factory('Aditivo', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/aditivo/:id/:action', null, {
            remover: {
                method: 'GET',
                params: {
                    'action': 'remover'
                }
            }
        });
    }])

    /* FUNDOFIXO */
    .factory('FundoFixo', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/fundofixo/:id/:action', null, {
            buscarQtdFundoFixoPorCfId: {
                method: 'GET',
                url: $restServer + 'rest/fundofixo/buscarqtdfundofixoporcf/:id'
            }
        });
    }])

    /* UNIDADE MEDIDA */
    .factory('UnidadeMedida', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/unidademedida/:id/:action', null, {
            buscarTodasUnidadesMedida: {
                method: 'GET',
                params: {
                    'action': 'buscarTodasUnidadesMedida'
                },
                isArray: true
            }

        });
    }])

    /* CATEGORIA DESPESA GERAL */
    .factory('CategoriaDespesaGeral', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/categoriadespesageral/:id/:action', null, {
            buscarTodasCategorias: {
                method: 'GET',
                params: {
                    'action': 'buscarTodasCategorias'
                },
                isArray: true
            }

        });
    }])

    /* CATEGORIA MÃO DE OBRA */
    .factory('CategoriaMaoDeObra', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/categoriamaodeobra/:id/:action', null, {
            buscarTodasCategorias: {
                method: 'GET',
                params: {
                    'action': 'buscarTodasCategorias'
                },
                isArray: true
            }

        });
    }])

    /* CATEGORIA MÃO DE OBRA ADICIONAL */
    .factory('CategoriaMaoDeObraAdicional', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/categoriamaodeobraadicional/:id/:action', null, {
            buscarTodasCategorias: {
                method: 'GET',
                params: {
                    'action': 'buscarTodasCategorias'
                },
                isArray: true
            }
        });
    }])


    /* PAGAMENTO */
    .factory('Pagamento', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/pagamento/:beforeAction/:id/:action', null, {
            popup: {
                method: 'GET',
                url: $restServer + 'rest/pagamento/popup/:id/:tipo/:codNota',
            },
            getNotaPagamento: {
                method: 'GET',
                params: {
                    'beforeAction': 'notapagamento'
                },
            },
            getCodigoNota: {
                method: 'GET',
                params: {
                    'beforeAction': 'codigonota'
                },
            }
        });
    }])

    /* FERIADO */
    .factory('Feriado', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/feriado/:id/:action', null, {
            buscarTodos: {
                method: 'GET',
                params: {
                    'action': 'buscarTodos'
                },
                isArray: true
            },
            getDataPagamentoDataAgendamento: {
                method: 'GET',
                url: $restServer + 'rest/feriado/data/pagamento/agendamento?dataVencimento=:dataVencimento'
            },
        });
    }])


    /* GRÁFICO FATURAMENTO/DESPESA */
    .factory('GraficoFaturamentoDespesa', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/graficofaturamentodespesas/:action', null, {
            buscarCLs: {
                method: 'POST',
                params: {
                    'action': 'buscarCLs'
                },
                isArray: true
            },
            gerarGraficoFaturamento: {
                method: 'POST',
                params: {
                    'action': 'gerarGraficoFaturamento'
                }
            },
            gerarGraficoReceitaLiquida: {
                method: 'POST',
                params: {
                    'action': 'gerarGraficoReceitaLiquida'
                }
            },
            gerarGraficoCustos: {
                method: 'POST',
                params: {
                    'action': 'gerarGraficoCustos'
                }
            },
            gerarGraficoMargem: {
                method: 'POST',
                params: {
                    'action': 'gerarGraficoMargem'
                }
            }

        });
    }])

    /* MOTIVO DESCONTO DEVOLUCAO */
    .factory('MotivoDescontoDevolucao', ['$resource', '$restServer', function($resource, $restServer) {
        return $resource($restServer + 'rest/motivo/desconto/devolucao/:beforeAction/:id/:action', null, {
            getListaDescontoByCodNota: {
                method: 'GET',
                params: {
                    'beforeAction': 'getListaDescontoByCodNota'
                },
                isArray: true
            },
            getListaDevolucaoByCodNota: {
                method: 'GET',
                params: {
                    'beforeAction': 'getListaDevolucaoByCodNota'
                },
                isArray: true
            }
        });
    }]);
