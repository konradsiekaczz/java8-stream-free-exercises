package pl.klolo.workshops.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.mock.HoldingMockGenerator;

class WorkShop {
    /**
     * Predykat określający czy użytkownik jest kobietą
     **/
    public static final Predicate<User> IS_WOMEN = user -> user.getSex().equals(Sex.WOMAN);
    /**
     * Lista holdingów wczytana z mocka.
     */
    private final List<Holding> holdings;

    // Predykat określający czy użytkownik jest kobietą
    private final Predicate<User> isWoman = null;

    WorkShop() {
        final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
        holdings = holdingMockGenerator.generate();
    }

    /**
     * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
     */
    long getHoldingsWhereAreCompanies() {
        for (int i = 0; i < holdings.size(); i++) {
            if (holdings.get(i).getCompanies().size() >= 1) {
                return holdings.size();
            }
        }
        return -1;
    }

    /**
     * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma. Napisz to za pomocą strumieni.
     */
    long getHoldingsWhereAreCompaniesAsStream() {
        return holdings.stream()
                .filter(holding -> holding.getCompanies().size() >= 1)
                .count();
    }

    /**
     * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
     */
    List<String> getHoldingNames() {
        String regex = "^[a-z]*";
        List<String> holdingsToLowerCase = new ArrayList<>();
        for (int i = 0; i < holdings.size(); i++) {
            holdingsToLowerCase.add(holdings.get(i).getName().toLowerCase());
        }
        return holdingsToLowerCase;
    }


    /**
     * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy. Napisz to za pomocą strumieni.
     */
    List<String> getHoldingNamesAsStream() {
        return holdings.stream()
                .map(holding -> holding.getName().toLowerCase())
                .collect(Collectors.toList());
    }

    /**
     * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico)
     */
    String getHoldingNamesAsString() {
        StringBuilder concateNamesOfHoldings = new StringBuilder();
        List<String> namesOfHoldings = new ArrayList<>();
        for (Holding holding : holdings) {
            namesOfHoldings.add(holding.getName());
        }
        Collections.sort(namesOfHoldings);
        for (String namesOfHolding : namesOfHoldings) {
            concateNamesOfHoldings.append(namesOfHolding).append(", ");
        }
        return "(" + concateNamesOfHoldings.substring(0, concateNamesOfHoldings.length() - 2) + ")";

    }

    /**
     * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico). Napisz to za pomocą strumieni.
     */
    String getHoldingNamesAsStringAsStream() {
        String namesOfHoldings = holdings.stream()
                .map(Holding::getName)
                .sorted()
                .collect(Collectors.joining(", ", "(", ")"));
        return namesOfHoldings;
    }

    /**
     * Zwraca liczbę firm we wszystkich holdingach.
     */
    long getCompaniesAmount() {
        long numberOfAllCompanies = 0;
        for (Holding holding : holdings) {
            numberOfAllCompanies += holding.getCompanies().size();
        }
        return numberOfAllCompanies;
    }

    /**
     * Zwraca liczbę firm we wszystkich holdingach. Napisz to za pomocą strumieni.
     */
    long getCompaniesAmountAsStream() {
        return holdings.stream()
                .mapToInt(holding -> holding.getCompanies().size())
                .sum();
        //or
//        return holdings.stream()
//                .map(holding -> holding.getCompanies().size())
//                .reduce(0,Integer::sum);
    }

    /**
     * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
     */
    long getAllUserAmount() {

        int numberOfAllUsersFromAllCompanies = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                numberOfAllUsersFromAllCompanies += company.getUsers().size();
            }
        }
        return numberOfAllUsersFromAllCompanies;
    }

    /**
     * Zwraca liczbę wszystkich pracowników we wszystkich firmach. Napisz to za pomocą strumieni.
     */
    long getAllUserAmountAsStream() {

        return holdings.stream()
                .flatMap(holding -> holding.getCompanies().stream())
                .mapToLong(company -> company.getUsers().size())
                .sum();
    }

    /**
     * Zwraca listę wszystkich nazw firm w formie listy.
     */
    List<String> getAllCompaniesNames() {
        List<String> namesOfCompanies = new ArrayList<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                namesOfCompanies.add(company.getName());
            }
        }
        return namesOfCompanies;
    }

    /**
     * Zwraca listę wszystkich nazw firm w formie listy. Tworzenie strumienia firm umieść w osobnej metodzie którą później będziesz wykorzystywać. Napisz to za
     * pomocą strumieni.
     */
    List<String> getAllCompaniesNamesAsStream() {
        return getCompaniesInStream()
                .map(Company::getName)
                .collect(Collectors.toList());

        //or
//        return holdings.stream()
//                .flatMap(holding -> holding.getCompanies().stream())
//                .map(Company::getName)
//                .collect(Collectors.toList());
    }

    private Stream<Company> getCompaniesInStream() {
        return holdings.stream()
                .flatMap(holding -> holding.getCompanies().stream());

    }

    /**
     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList.
     */
    LinkedList<String> getAllCompaniesNamesAsLinkedList() {
        LinkedList<String> allCompaniesNames = new LinkedList<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                allCompaniesNames.add(company.getName());
            }

        }
        return allCompaniesNames;
    }

    /**
     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy po zakończeniu działania strumienia. Napisz to za
     * pomocą strumieni.
     */
    LinkedList<String> getAllCompaniesNamesAsLinkedListAsStream() {
        return getCompaniesInStream()
                .map(Company::getName)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
     */
    String getAllCompaniesNamesAsString() {
        List<String> namesOfAllCompany = getCompaniesInStream()
                .map(Company::getName)
                .collect(Collectors.toList());
        StringBuilder result = new StringBuilder();
        int numberOfCompaniesNames = namesOfAllCompany.size();
        int count = 0;
        for (String name : namesOfAllCompany) {
            result.append(name);
            count++;
            if (count < numberOfCompaniesNames) {
                result.append("+");
            }
        }
        return result.toString();
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+" Napisz to za pomocą strumieni.
     */
    String getAllCompaniesNamesAsStringAsStream() {
        return getCompaniesInStream()
                .map(Company::getName)
                .collect(Collectors.joining("+"));
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+". Używamy collect i StringBuilder. Napisz to za pomocą
     * strumieni.
     * <p>
     * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
     */
    String getAllCompaniesNamesAsStringUsingStringBuilder() {
        return getCompaniesInStream()
                .map(Company::getName)
                .collect(Collector.of(StringBuilder::new,
                        (stringBuilder, s) -> stringBuilder.append(s).append("+"),
                        StringBuilder::append,
                        stringBuilder -> stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length()))).toString();
    }

    /**
     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
     */
    long getAllUserAccountsAmount() {
        long numberOffAllAccounts = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    numberOffAllAccounts += user.getAccounts().size();
                }
            }
        }
        return numberOffAllAccounts;
    }

    /**
     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach. Napisz to za pomocą strumieni.
     */
    long getAllUserAccountsAmountAsStream() {
        return getCompaniesInStream()
                .flatMap(company -> company.getUsers().stream())
                .flatMap(user -> user.getAccounts().stream())
                .count();
    }

    /**
     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane.
     */
    String getAllCurrencies() {
        List<Currency> currencies = Arrays.asList(Currency.values());

        Collections.sort(currencies, Comparator.comparing(Enum::toString));

        StringBuilder allCurrencies = new StringBuilder();

        for (Currency c : currencies) {
            allCurrencies.append(c.toString());
            allCurrencies.append(", ");
        }

        allCurrencies.delete(allCurrencies.length() - 2, allCurrencies.length());
        return allCurrencies.toString();
    }

    /**
     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane. Napisz to za pomocą strumieni.
     */
    String getAllCurrenciesAsStream() {
        return Stream.of(Currency.values())
                .sorted(Comparator.comparing(Enum::toString))
                .collect(Collector.of(StringBuilder::new,
                        (stringBuilder, s) -> stringBuilder.append(s).append(", "),
                        StringBuilder::append,
                        stringBuilder -> stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()))).toString();
    }

    /**
     * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody stream, tylko skorzystaj z  Stream.generate.
     * Wspólny kod wynieś do osobnej metody.
     *
     * @see #getAllCurrencies()
     */

    String getAllCurrenciesUsingGenerate() {
        List<String> currencies = getAllCurrenciesAsList();
        return Stream.generate(currencies.iterator()::next)
                .collect(Collectors.joining(", "));
    }

    private List<String> getAllCurrenciesAsList(){
        return getCompanyStream()
                .flatMap(company -> company.getUsers().stream())
                .flatMap(user -> user.getAccounts().stream())
                .map(account -> account.getCurrency().toString())
                .sorted(String::compareTo)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Zwraca liczbę kobiet we wszystkich firmach.
     */
    long getWomanAmount() {
        int numberOfWomenFromAllCompany = 0;

        for (Holding h : holdings) {
            for (Company c : h.getCompanies()) {
                for (User u : c.getUsers()) {
                    if (u.getSex().equals(Sex.WOMAN)) {
                        numberOfWomenFromAllCompany++;
                    }
                }
            }
        }

        return numberOfWomenFromAllCompany;
    }

    /**
     * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień uzytkowników umieść w osobnej metodzie. Predicate określający
     * czy mamy doczynienia z kobietą inech będzie polem statycznym w klasie. Napisz to za pomocą strumieni.
     */
    long getWomanAmountAsStream() {
        return getUserStream()
                .filter(IS_WOMEN)
                .count();
    }


    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Ustaw precyzje na 3 miejsca po przecinku.
     */
    BigDecimal getAccountAmountInPLN(final Account account) {
        return account.getAmount()
                .multiply(BigDecimal.valueOf(account.getCurrency().rate)).setScale(3, RoundingMode.HALF_DOWN);

//        BigDecimal amount = null;
//        for (Holding h: holdings) {
//            for (Company c: h.getCompanies()) {
//                for (User u : c.getUsers()) {
//                    for (Account a: u.getAccounts()) {
//                         amount = a.getAmount().multiply(BigDecimal.valueOf(a.getCurrency().rate));
//                    }
//                }
//            }
//        }
//        return amount;

    }


    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Napisz to za pomocą strumieni.
     */
    BigDecimal getAccountAmountInPLNAsStream(final Account account) {

        return  Stream.of(account)
                .map(account1 -> account1.getAmount().multiply(BigDecimal.valueOf(account.getCurrency().rate)).setScale(3,RoundingMode.HALF_DOWN))
                .findFirst()
                .get();

//        return account.getAmount()
//                .multiply(BigDecimal.valueOf(account.getCurrency().rate)).setScale(3, RoundingMode.HALF_DOWN);
    }

    /**
     * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją.
     */
    BigDecimal getTotalCashInPLN(final List<Account> accounts) {

        BigDecimal amount = BigDecimal.ZERO;
        for (Account a: accounts) {
            amount = amount.add(a.getAmount().multiply(BigDecimal.valueOf(a.getCurrency().rate)));
        }
        return amount;
    }

    /**
     * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją. Napisz to za pomocą strumieni.
     */
    BigDecimal getTotalCashInPLNAsStream(final List<Account> accounts) {
        return accounts.stream()
                .map(account -> account.getAmount().multiply(BigDecimal.valueOf(account.getCurrency().rate)))
                .reduce(new BigDecimal("0.0"), BigDecimal::add);
    }

    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
     */
    Set<String> getUsersForPredicate(final Predicate<User> userPredicate) {
        Set<String> usersNamesFromPredicate = new HashSet<>();
        for (Holding holding: holdings) {
            for (Company company: holding.getCompanies()) {
                for (User user: company.getUsers()) {
                    if(userPredicate.test(user)){
                        usersNamesFromPredicate.add(user.getFirstName());
                    }
                }
            }
        }
        System.out.println(usersNamesFromPredicate);
        return usersNamesFromPredicate;
    }

    /**
     * Zwraca imię użytkownika jeśli spełnia podany warunek.
     */
    private Predicate<User> allUserWithNameStartAtM(){
        return user -> user.getFirstName().startsWith("M");
    }

    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek. Napisz to za pomocą strumieni.
     */
    Set<String> getUsersForPredicateAsStream(final Predicate<User> userPredicate) {
        return getUserStream()
                .filter(userPredicate)
                .map(User::getFirstName)
                .collect(Collectors.toSet());
    }

    /**
     * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy.
     */
    List<String> getOldWoman(final int age) {
        List<String> womensOlderThanSpecificAge = new ArrayList<>();
        for (Holding holiding:holdings) {
            for (Company company:holiding.getCompanies()) {
                for (User user: company.getUsers()) {
                    if(user.getAge()>age && user.getSex().equals(Sex.WOMAN)){
                        womensOlderThanSpecificAge.add(user.getFirstName());
                    }
                }
            }
        }
        return womensOlderThanSpecificAge;
    }

    /**
     * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy. Napisz
     * to za pomocą strumieni.
     */
    List<String> getOldWomanAsStream(final int age) {
        return getUserStream()
                .filter(user -> user.getAge()>age && user.getSex().equals(Sex.WOMAN))
                .map(user -> user.getFirstName())
                .collect(Collectors.toList());
    }

    /**
     * Dla każdej firmy uruchamia przekazaną metodę.
     */
    void executeForEachCompany(final Consumer<Company> consumer) {
        throw new IllegalArgumentException();
    }

    /**
     * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach.
     */
    Optional<User> getRichestWoman() {
        return null;
    }

    /**
     * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach. Napisz to za pomocą strumieni.
     */
    Optional<User> getRichestWomanAsStream() {
        return null;
    }

    /**
     * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
     */
    Set<String> getFirstNCompany(final int n) {
        return null;
    }

    /**
     * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia. Napisz to za pomocą strumieni.
     */
    Set<String> getFirstNCompanyAsStream(final int n) {
        return null;
    }

    /**
     * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
     * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return.
     */
    AccountType getMostPopularAccountType() {
        return null;
    }

    /**
     * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
     * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return. Napisz to za pomocą strumieni.
     */
    AccountType getMostPopularAccountTypeAsStream() {
        return null;
    }

    /**
     * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException.
     */
    User getUser(final Predicate<User> predicate) {
        return null;
    }

    /**
     * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException. Napisz to
     * za pomocą strumieni.
     */
    User getUserAsStream(final Predicate<User> predicate) {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
     */
    Map<String, List<User>> getUserPerCompany() {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników. Napisz to za pomocą strumieni.
     */
    Map<String, List<User>> getUserPerCompanyAsStream() {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
     * Możesz skorzystać z metody entrySet.
     */
    Map<String, List<String>> getUserPerCompanyAsString() {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
     * Możesz skorzystać z metody entrySet. Napisz to za pomocą strumieni.
     */
    Map<String, List<String>> getUserPerCompanyAsStringAsStream() {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej
     * funkcji.
     */
    <T> Map<String, List<T>> getUserPerCompany(final Function<User, T> converter) {
        return null;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej funkcji.
     * Napisz to za pomocą strumieni.
     */
    <T> Map<String, List<T>> getUserPerCompanyAsStream(final Function<User, T> converter) {
        return null;
    }

    /**
     * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
     * jest natomiast zbiór nazwisk tych osób.
     */
    Map<Boolean, Set<String>> getUserBySex() {
        return null;
    }

    /**
     * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
     * jest natomiast zbiór nazwisk tych osób. Napisz to za pomocą strumieni.
     */
    Map<Boolean, Set<String>> getUserBySexAsStream() {
        return null;
    }

    /**
     * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek.
     */
    Map<String, Account> createAccountsMap() {
        return null;
    }

    /**
     * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek. Napisz to za pomocą strumieni.
     */
    Map<String, Account> createAccountsMapAsStream() {
        return null;
    }

    /**
     * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
     */
    String getUserNames() {
        return null;
    }

    /**
     * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń. Napisz to za pomocą strumieni.
     */
    String getUserNamesAsStream() {
        return null;
    }

    /**
     * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
     */
    Set<User> getUsers() {
        return null;
    }

    /**
     * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10. Napisz to za pomocą strumieni.
     */
    Set<User> getUsersAsStream() {
        return null;
    }

    /**
     * Zwraca użytkownika, który spełnia podany warunek.
     */
    Optional<User> findUser(final Predicate<User> userPredicate) {
        return null;
    }

    /**
     * Zwraca użytkownika, który spełnia podany warunek. Napisz to za pomocą strumieni.
     */
    Optional<User> findUserAsStream(final Predicate<User> userPredicate) {
        return null;
    }

    /**
     * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie: IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak
     * użytkownika.
     * <p>
     * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów. Napisz to za pomocą strumieni.
     */
    String getAdultantStatusAsStream(final Optional<User> user) {
        return null;
    }

    /**
     * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
     * Pasibrzuch, Adam Wojcik
     */
    void showAllUser() {
        throw new IllegalArgumentException("not implemented yet");
    }

    /**
     * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
     * Pasibrzuch, Adam Wojcik. Napisz to za pomocą strumieni.
     */
    void showAllUserAsStream() {
        throw new IllegalArgumentException("not implemented yet");
    }

    /**
     * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki.
     */
    Map<AccountType, BigDecimal> getMoneyOnAccounts() {
        return null;
    }

    /**
     * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki. Napisz to za pomocą
     * strumieni. Ustaw precyzje na 0.
     */
    Map<AccountType, BigDecimal> getMoneyOnAccountsAsStream() {
        return null;
    }

    /**
     * Zwraca sumę kwadratów wieków wszystkich użytkowników.
     */
    int getAgeSquaresSum() {
        return -1;
    }

    /**
     * Zwraca sumę kwadratów wieków wszystkich użytkowników. Napisz to za pomocą strumieni.
     */
    int getAgeSquaresSumAsStream() {
        return -1;
    }

    /**
     * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
     * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody).
     */
    List<User> getRandomUsers(final int n) {
        return null;
    }

    /**
     * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
     * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody). Napisz to za pomocą strumieni.
     */
    List<User> getRandomUsersAsStream(final int n) {
        return null;
    }

    /**
     * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
     * na rachunku danego typu przeliczona na złotkówki.
     */
    Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMap() {
        return null;
    }

    /**
     * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
     * na rachunku danego typu przeliczona na złotkówki.  Napisz to za pomocą strumieni.
     */
    Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMapAsStream() {
        return null;
    }

    /**
     * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
     * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach.
     */

    Map<Permit, List<User>> getUsersByTheyPermitsSorted() {
        return null;
    }

    /**
     * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
     * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach. Napisz to za pomoca strumieni.
     */

    Map<Permit, List<User>> getUsersByTheyPermitsSortedAsStream() {
        return null;
    }

    /**
     * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
     * List<Users>
     */
    Map<Boolean, List<User>> divideUsersByPredicate(final Predicate<User> predicate) {
        return null;
    }

    /**
     * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
     * List<Users>. Wykonaj zadanie za pomoca strumieni.
     */
    Map<Boolean, List<User>> divideUsersByPredicateAsStream(final Predicate<User> predicate) {
        return null;
    }

    /**
     * Zwraca strumień wszystkich firm.
     */
    private Stream<Company> getCompanyStream() {
        return holdings.stream()
                .flatMap(holding -> holding.getCompanies().stream());
    }

    /**
     * Zwraca zbiór walut w jakich są rachunki.
     */
    private Set<Currency> getCurenciesSet() {
        return null;
    }

    /**
     * Tworzy strumień rachunków.
     */
    private Stream<Account> getAccountStream() {
        return null;
    }

    /**
     * Tworzy strumień użytkowników.
     */
    private Stream<User> getUserStream() {
        return getCompaniesInStream()
                .flatMap(company -> company.getUsers().stream());
    }

}
