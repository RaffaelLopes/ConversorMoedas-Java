import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMoedasAvancado {

    private static final String API_KEY = "de878ce1f032af4dbb86fced";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private static final long CACHE_DURATION_MS = 30 * 60 * 1000; // 30 minutos

    private static Map<String, TaxaCache> cacheTaxas = new HashMap<>();
    private static List<String> historico = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== CONVERSOR DE MOEDAS AVANÇADO ===");
        System.out.println("Bem-vindo ao conversor de moedas com taxas em tempo real!");

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1: converterMoeda("USD", "BRL", "Dólar Americano", "Real Brasileiro"); break;
                case 2: converterMoeda("EUR", "BRL", "Euro", "Real Brasileiro"); break;
                case 3: converterMoeda("GBP", "BRL", "Libra Esterlina", "Real Brasileiro"); break;
                case 4: converterMoeda("BRL", "USD", "Real Brasileiro", "Dólar Americano"); break;
                case 5: converterMoeda("JPY", "BRL", "Iene Japonês", "Real Brasileiro"); break;
                case 6: converterMoeda("BRL", "EUR", "Real Brasileiro", "Euro"); break;
                case 7: converterMoeda("CAD", "BRL", "Dólar Canadense", "Real Brasileiro"); break;
                case 8: converterMoeda("AUD", "BRL", "Dólar Australiano", "Real Brasileiro"); break;
                case 9: converterMoeda("CNY", "BRL", "Yuan Chinês", "Real Brasileiro"); break;
                case 10: converterMoedaPersonalizada(); break;
                case 11: exibirHistorico(); break;
                case 0: System.out.println("Saindo do conversor de moedas. Até logo!"); break;
                default: System.out.println("Opção inválida. Tente novamente.");
            }

            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
                scanner.nextLine();
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. USD → BRL (Dólar Americano → Real)");
        System.out.println("2. EUR → BRL (Euro → Real)");
        System.out.println("3. GBP → BRL (Libra → Real)");
        System.out.println("4. BRL → USD (Real → Dólar Americano)");
        System.out.println("5. JPY → BRL (Iene → Real)");
        System.out.println("6. BRL → EUR (Real → Euro)");
        System.out.println("7. CAD → BRL (Dólar Canadense → Real)");
        System.out.println("8. AUD → BRL (Dólar Australiano → Real)");
        System.out.println("9. CNY → BRL (Yuan Chinês → Real)");
        System.out.println("10. Conversão personalizada");
        System.out.println("11. Exibir histórico de conversões");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Por favor, digite um número: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void converterMoeda(String de, String para, String nomeDe, String nomePara) {
        try {
            System.out.printf("\n=== %s (%s) → %s (%s) ===\n", nomeDe, de, nomePara, para);
            System.out.print("Digite o valor em " + nomeDe + ": ");
            double valor = scanner.nextDouble();

            double taxa = obterTaxaConversao(de, para);
            if (taxa == -1) return;

            double valorConvertido = valor * taxa;
            String registro = String.format("%.2f %s = %.2f %s (Taxa: 1 %s = %.6f %s)",
                    valor, de, valorConvertido, para, de, taxa, para);

            System.out.println("\n" + registro);
            adicionarHistorico(registro);
        } catch (Exception e) {
            System.out.println("Erro: Valor inválido. " + e.getMessage());
            scanner.nextLine();
        }
    }

    private static void converterMoedaPersonalizada() {
        try {
            scanner.nextLine();

            System.out.println("\n=== CONVERSÃO PERSONALIZADA ===");
            System.out.print("Moeda de origem (ex: USD, EUR, BRL): ");
            String de = scanner.nextLine().toUpperCase();

            System.out.print("Moeda de destino (ex: BRL, JPY, GBP): ");
            String para = scanner.nextLine().toUpperCase();

            System.out.print("Valor a converter: ");
            double valor = scanner.nextDouble();

            double taxa = obterTaxaConversao(de, para);
            if (taxa == -1) return;

            double valorConvertido = valor * taxa;
            String registro = String.format("%.2f %s = %.2f %s (Taxa: 1 %s = %.6f %s)",
                    valor, de, valorConvertido, para, de, taxa, para);

            System.out.println("\n" + registro);
            adicionarHistorico(registro);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private static double obterTaxaConversao(String de, String para) {
        String chaveCache = de + "_" + para;

        // Verificar cache
        if (cacheTaxas.containsKey(chaveCache)) {
            TaxaCache cached = cacheTaxas.get(chaveCache);
            if (System.currentTimeMillis() - cached.timestamp < CACHE_DURATION_MS) {
                System.out.println("\n(Usando taxa do cache)");
                return cached.taxa;
            }
        }

        try {
            System.out.println("\n(Obtendo taxa atual da API...)");
            URL url = new URL(BASE_URL + de + "/" + para);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            int respostaCodigo = conexao.getResponseCode();
            if (respostaCodigo != 200) {
                System.out.println("Erro ao acessar a API. Usando última taxa conhecida...");
                return cacheTaxas.containsKey(chaveCache) ? cacheTaxas.get(chaveCache).taxa : -1;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            reader.close();

            JSONObject jsonResposta = new JSONObject(resposta.toString());
            if (!jsonResposta.getString("result").equals("success")) {
                System.out.println("Erro na API: " + jsonResposta.getString("error-type"));
                return -1;
            }

            double taxa = jsonResposta.getDouble("conversion_rate");
            cacheTaxas.put(chaveCache, new TaxaCache(taxa, System.currentTimeMillis()));
            return taxa;
        } catch (Exception e) {
            System.out.println("Erro ao conectar com a API: " + e.getMessage());
            return -1;
        }
    }

    private static void adicionarHistorico(String registro) {
        historico.add(registro);
        if (historico.size() > 10) {
            historico.remove(0);
        }
    }

    private static void exibirHistorico() {
        System.out.println("\n=== ÚLTIMAS 10 CONVERSÕES ===");
        if (historico.isEmpty()) {
            System.out.println("Nenhuma conversão realizada ainda.");
        } else {
            for (int i = 0; i < historico.size(); i++) {
                System.out.println((i+1) + ". " + historico.get(i));
            }
        }
    }

    static class TaxaCache {
        double taxa;
        long timestamp;

        TaxaCache(double taxa, long timestamp) {
            this.taxa = taxa;
            this.timestamp = timestamp;
        }
    }
}