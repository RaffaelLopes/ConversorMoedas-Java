Conversor de Moedas em Java 💰➡️💱
Java
GitHub last commit
License

Um conversor de moedas robusto desenvolvido em Java que obtém taxas de câmbio em tempo real através de API externa, com interface intuitiva via console.

✨ Funcionalidades
Conversão entre 6+ moedas pré-definidas

Conversão personalizada (qualquer par de moedas)

Taxas atualizadas via API em tempo real

Histórico das últimas conversões

Interface amigável em português

📋 Moedas Suportadas
Código	Moeda
USD	Dólar Americano
EUR	Euro
GBP	Libra Esterlina
JPY	Iene Japonês
BRL	Real Brasileiro
CAD	Dólar Canadense
AUD	Dólar Australiano
CNY	Yuan Chinês
🚀 Como Executar
Pré-requisitos
Java JDK 17+

Conexão com internet (para acesso à API)

Passo a Passo
Clone o repositório:

bash
git clone https://github.com/RaffaelLopes/ConversorMoedas-Java.git
Importe no IntelliJ como projeto Java

Execute a classe principal:

src/ConversorMoedasAvancado.java

Siga as instruções no console

🌐 API Utilizada
ExchangeRate-API v6
API Status
Fornece taxas de câmbio atualizadas com precisão bancária.

🛠 Estrutura do Projeto
ConversorMoedas/
├── src/
│   └── ConversorMoedasAvancado.java  # Código principal
├── libs/
│   └── json-20231013.jar             # Biblioteca JSON
├── .gitignore
└── README.md


<div align="center"> <p>Desenvolvido com ❤️ por <a href="https://github.com/RaffaelLopes">Raffael Lopes</a></p> <img src="https://img.shields.io/badge/Java-Expert-orange?style=for-the-badge"> </div>
💡 Dica para Desenvolvedores
Para adicionar novas moedas:

Modifique o método exibirMenu() em ConversorMoedasAvancado.java

Adicione novos casos no switch-case

Teste com:

java
converterMoeda("NOVO_CODIGO", "BRL", "Nova Moeda", "Real Brasileiro");
Contribuições são bem-vindas! Envie seus PRs. 🚀

Open in IntelliJ

