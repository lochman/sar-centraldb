package cz.zcu.sar.centraldb.common.utils;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Matej Lochman on 5.1.17.
 */
public class BasicTestDataGenerator<P extends PersonWrapper, A extends AddressWrapper,
        PT extends PersonTypeWrapper, AT extends AddressTypeWrapper> implements TestDataGenerator<P, A, PT, AT> {
    private final String[] firstNamesMale = {"Jiří", "Jan", "Petr", "Josef", "Pavel", "Jaroslav", "Martin", "Miroslav", "Tomáš", "František", "Zdeněk", "Václav", "Karel", "Milan", "Michal"};
    private final String[] firstNamesFemale = {"Marie", "Jana", "Eva", "Anna", "Hana", "Věra", "Lenka", "Alena", "Kateřina", "Petra", "Lucie", "Jaroslava", "Ludmila", "Helena","Jitka"};

    private final String[] lastNamesMale = {"Novák", "Svoboda", "Novotný", "Dvořák", "Černý", "Procházka", "Kučera", "Veselý", "Horák", "Němec", "Pokorný", "Marek", "Pospíšil", "Hájek", "Jelínek"};
    private final String[] lastNamesFemale = {"Nováková", "Svobodová", "Novotná", "Dvořáková", "Černá", "Procházková", "Kučerová", "Veselá", "Horáková", "Němcová", "Pokorná", "Marková", "Pospíšilová", "Hájková", "Jelínková"};

    private final String[] companyNamesStart = {"","Velké ", "Malé ", "Soukromé ", "Rodinné ", "České "};
    private final String[] companyNamesMiddle = {"Problémy", "Služby", "Vinařství", "Rybářství", "Řeznictví", "Pekařství", "Zámečnicství", "Stavařství", "Ocelářství", "Zelinářtví"};
    private final String[] companyNamesEnd= {"",", a.s.", ", s.r.o"};

    private final String[] streetNames = {"Pařížská","Dlouhá", "Zahradní", "Příkrá", "Olšová", "Alešova", "Spojovací"};
    private final String[] cityNames = {"Praha", "Brno", "Plzeň", "Karlovy Vary", "Ždár nad Sázavou"};

    private final String[] personTypes = {"Fyzická osoba", "Právnická osoba"};
    private final String[] addressTypes = {"Trvalá adresa", "Přechodná adresa"};
    private Class<P> personClass;
    private Class<A> addressClass;

    public BasicTestDataGenerator(Class<P> personClass, Class<A> addressClass) {
        this.personClass = personClass;
        this.addressClass = addressClass;
    }

    @Override
    public P nextPerson(PT personType) {
        return ThreadLocalRandom.current().nextInt(2) == 0 ? nextPerson(personType, true) : nextPerson(personType, false);
    }

    @Override
    public P nextPerson(PT personType, boolean male) {
        P person = null;
        try {
            person = personClass.newInstance();
            if (male) {
                person.setFirstName(firstNamesMale[ThreadLocalRandom.current().nextInt(firstNamesMale.length)]);
                person.setName(lastNamesMale[ThreadLocalRandom.current().nextInt(lastNamesMale.length)]);
                person.setBirthName(person.getName());
                person.setGender("m");
            } else {
                person.setFirstName(firstNamesFemale[ThreadLocalRandom.current().nextInt(firstNamesFemale.length)]);
                person.setName(lastNamesFemale[ThreadLocalRandom.current().nextInt(lastNamesFemale.length)]);
                person.setBirthName(lastNamesFemale[ThreadLocalRandom.current().nextInt(lastNamesFemale.length)]);
                person.setGender("f");
            }
            person.setBirthDate(new Date(ThreadLocalRandom.current().nextLong(new Date().getTime())));
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
            person.setSocialNumber(formatter.format(person.getBirthDate())+ "/" + (ThreadLocalRandom.current().nextInt(9000)+1000));
            person.setPersonType(personType);
            person.setModifiedBy("init");
            // generate modified time
            long hour = 60 * 60 * 1000;
            int time = ThreadLocalRandom.current().nextInt(7 * 24);
            person.setModifiedTime(new Timestamp(System.currentTimeMillis() - (hour * time)));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public P nextCompany(PT personType) {
        P company = null;
        try {
            company = personClass.newInstance();
            company.setFirstName("");
            company.setBirthName("");
            company.setName(companyNamesStart[ThreadLocalRandom.current().nextInt(companyNamesStart.length)] +
                    companyNamesMiddle[ThreadLocalRandom.current().nextInt(companyNamesMiddle.length)] +
                    companyNamesEnd[ThreadLocalRandom.current().nextInt(companyNamesEnd.length)]);
            company.setBirthDate(new Date(ThreadLocalRandom.current().nextLong(new Date().getTime())));
            company.setCompanyNumber(String.valueOf(ThreadLocalRandom.current().nextInt(100000000)+100000));
            company.setPersonType(personType);
            company.setModifiedBy("init");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public A nextAddress(AT addressType) {
        A address = null;
        try {
            address = addressClass.newInstance();
            address.setAddressType(addressType);
            address.setStreet(streetNames[ThreadLocalRandom.current().nextInt(streetNames.length)]);
            address.setLand_registry_number(String.valueOf(ThreadLocalRandom.current().nextInt(40)+1));
            address.setCity(cityNames[ThreadLocalRandom.current().nextInt(cityNames.length)]);
            address.setModifiedBy("init");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return address;
    }
}
