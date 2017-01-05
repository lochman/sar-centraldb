package cz.zcu.sar.centraldb.web;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Matej Lochman on 22.12.16.
 */

@Component
public class TestDataLoader implements ApplicationRunner {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addresssRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    private final String[] firstNamesMale = {"Jiří", "Jan", "Petr", "Josef", "Pavel", "Jaroslav", "Martin", "Miroslav", "Tomáš", "František", "Zdeněk", "Václav", "Karel", "Milan", "Michal"};
    private final String[] firstNamesFemale = {"Marie", "Jana", "Eva", "Anna", "Hana", "Věra", "Lenka", "Alena", "Kateřina", "Petra", "Lucie", "Jaroslava", "Ludmila", "Helena","Jitka"};

    private final String[] lastNamesMale = {"Novák", "Svoboda", "Novotný", "Dvořák", "Černý", "Procházka", "Kučera", "Veselý", "Horák", "Němec", "Pokorný", "Marek", "Pospíšil", "Hájek", "Jelínek"};
    private final String[] lastNamesFemale = {"Nováková", "Svobodová", "Novotná", "Dvořáková", "Černá", "Procházková", "Kučerová", "Veselá", "Horáková", "Němcová", "Pokorná", "Marková", "Pospíšilová", "Hájková", "Jelínková"};

    private final String[] companyNamesStart = {"","Velké ", "Malé ", "Soukromé ", "Rodinné ", "České "};
    private final String[] companyNamesMiddle = {"Problémy", "Služby", "Vinařství", "Rybářství", "Řeznictví", "Pekařství", "Zámečnicství", "Stavařství", "Ocelářství", "Zelinářtví"};
    private final String[] companyNamesEnd= {"",", a.s.", ", s.r.o"};

    private final String[] streetNames= {"Pařížská","Dlouhá", "Zahradní", "Příkrá", "Olšová", "Alešova", "Spojovací"};
    private final String[] cityNames= {"Praha", "Brno", "Plzeň", "Karlovy Vary", "Ždár nad Sázavou"};
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Person person;
        //Create person types
        PersonType t1 = new PersonType();
        t1.setDescription("Fyzická osoba");
        t1.setModifiedBy("init");
        personTypeRepository.save(t1);
        PersonType t2 = new PersonType();
        t2.setDescription("Právnická osoba");
        t2.setModifiedBy("init");
        personTypeRepository.save(t2);

        //Create address types
        AddressType at1 = new AddressType();
        at1.setDescription("Trvalá adresa");
        at1.setModifiedBy("init");
        addressTypeRepository.save(at1);
        AddressType at2 = new AddressType();
        at2.setDescription("Přechodná adresa");
        at2.setModifiedBy("init");
        addressTypeRepository.save(at2);
        for (int i = 0; i < 112; i++) {
            Address address = new Address();
            //company or person
            if (ThreadLocalRandom.current().nextInt(2) == 0){
                //person
                if (ThreadLocalRandom.current().nextInt(2) == 0){
                    //male
                    person = new Person(firstNamesMale[ThreadLocalRandom.current().nextInt(15)], lastNamesMale[ThreadLocalRandom.current().nextInt(15)], "m");
                }else{
                    //female
                    person = new Person(firstNamesFemale[ThreadLocalRandom.current().nextInt(15)], lastNamesFemale[ThreadLocalRandom.current().nextInt(15)], "f");
                }
                person.setPersonType(t1);
                //birthdate and socialnumber generation
                person.setBirthDate(new Date(ThreadLocalRandom.current().nextLong(new Date().getTime())));
                SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
                person.setSocialNumber(formatter.format(person.getBirthDate())+ "/" + (ThreadLocalRandom.current().nextInt(9000)+1000));
            }else{
                //company
                person = new Person("", companyNamesStart[ThreadLocalRandom.current().nextInt(6)]+
                        companyNamesMiddle[ThreadLocalRandom.current().nextInt(10)] + companyNamesEnd[ThreadLocalRandom.current().nextInt(3)],"");
                person.setPersonType(t2);
                person.setBirthDate(new Date(ThreadLocalRandom.current().nextLong(new Date().getTime())));
                person.setCompanyNumber(String.valueOf(ThreadLocalRandom.current().nextInt(100000000)+100000));
            }
            person.setModifiedBy("init");
            personRepository.save(person);
            //address
            address.setAddressType(at1);
            address.setStreet(streetNames[ThreadLocalRandom.current().nextInt(7)]);
            address.setLand_registry_number(String.valueOf(ThreadLocalRandom.current().nextInt(40)+1));
            address.setCity(cityNames[ThreadLocalRandom.current().nextInt(5)]);
            address.setModifiedBy("init");
            address.setPerson(person);
            //temporary address
            if( ThreadLocalRandom.current().nextInt(10) == 0) {
                Address address2 = new Address();
                address2.setAddressType(at2);address.setStreet(streetNames[ThreadLocalRandom.current().nextInt(7)]);
                address2.setLand_registry_number(String.valueOf(ThreadLocalRandom.current().nextInt(40)+1));
                address2.setCity(cityNames[ThreadLocalRandom.current().nextInt(5)]);
                address2.setModifiedBy("init");
                address2.setPerson(person);
                addresssRepository.save(address2);
            }
            addresssRepository.save(address);

        }
    }
}
