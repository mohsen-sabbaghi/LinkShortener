import ir.bki.entities.Links;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeConversionTest {

    @Test
    void dateTimeConversion() throws ParseException {

//[Links(id=7, shortLink=P8w, redirectLink=https://divar.ir/v/%D8%AF%D9%88%DA%86%D8%B1%D8%AE%D9%87-%D8%AE%D8%A7%D8%B1%D8%AC%DB%8C-%D9%86%D9%88_%D8%A7%D8%B3%D8%A8%D8%A7%D8%A8-%D8%A8%D8%A7%D8%B2%DB%8C_%D8%AA%D9%87%D8%B1%D8%A7%D9%86_%D8%A7%D9%85%DB%8C%D8%B1%D8%A2%D8%A8%D8%A7%D8%AF_%D8%AF%DB%8C%D9%88%D8%A7%D8%B1/AYh8Bbir, httpStatusCode=201, enabled=true, createdDate=Mon Aug 23 16:15:50 IRDT 2021, expiresDate=Fri Sep 03 00:00:00 IRDT 2021, activatedDate=null, description=null), Links(id=6, shortLink=jstl, redirectLink=https://mvnrepository.com/artifact/jstl/jstl/1.2, httpStatusCode=201, enabled=true, createdDate=Mon Aug 23 13:57:18 IRDT 2021, expiresDate=Sat Sep 04 00:00:00 IRDT 2021, activatedDate=null, description=null), Links(id=5, shortLink=PJ6, redirectLink=https://divar.ir/v/%D8%A2%D8%A8%DA%86%DA%A9%D8%A7%D9%86_%D9%87%D9%85%D9%87-%DB%8C-%D8%A2%DA%AF%D9%87%DB%8C-%D9%87%D8%A7%DB%8C-%D9%88%D8%B3%D8%A7%DB%8C%D9%84-%D8%A2%D8%B4%D9%BE%D8%B2%D8%AE%D8%A7%D9%86%D9%87_%D8%AA%D9%87%D8%B1%D8%A7%D9%86_%DB%8C%D8%A7%D9%81%D8%AA-%D8%A2%D8%A8%D8%A7%D8%AF_%D8%AF%DB%8C%D9%88%D8%A7%D8%B1/AYhoxRXj, httpStatusCode=201, enabled=true, createdDate=Mon Aug 23 13:09:32 IRDT 2021, expiresDate=Sat Sep 11 00:00:00 IRDT 2021, activatedDate=null, description=null), Links(id=4, shortLink=app, redirectLink=https://www.bki.ir/BankingServices/Ebanking/Systems/MobileBanking/MobileBankingHelp, httpStatusCode=201, enabled=true, createdDate=Mon Aug 23 13:01:58 IRDT 2021, expiresDate=Fri Dec 24 00:00:00 IRST 2021, activatedDate=null, description=null), Links(id=3, shortLink=console, redirectLink=http://localhost:8080/console/console.portal?_nfpb=true&_pageLabel=AppDeploymentsControlPage&handle=com.bea.console.handles.JMXHandle%28%22com.bea%3AName%3DLinkShortener%2CType%3DDomain%22%29, httpStatusCode=201, enabled=true, createdDate=Mon Aug 23 10:16:27 IRDT 2021, expiresDate=Mon Jan 31 00:00:00 IRST 2022, activatedDate=null, description=null)]
        Links l = new Links();
        l.setId(10);
        l.setCreatedDate(new Date());
        l.setExpiresDate(new Date());
        l.setEnabled(true);
        Links l2 = new Links();
        l2.setId(20);
        l2.setCreatedDate(new Date());
        l2.setExpiresDate(new Date());
        l2.setEnabled(true);

        List<Links> linksList = new ArrayList<>();
        linksList.add(l);
        linksList.add(l2);

        System.err.println(linksList);

        linksList.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));

        DateFormat dateFormat = new SimpleDateFormat(
                "E M dd HH:mm:ss Z yyyy", Locale.getDefault());
        dateFormat.parse("Mon Aug 23 13:57:18 IRDT 2021");
        System.out.println(dateFormat.format(new Date()));

//        DateTimeFormatter formatter = new DateTimeFormatter("EEEE M d HH:mm:ss Z Y");
//        DateTime dt = formatter.parseDateTime(linksList.get(0).getCreatedDate().toString());
//        DateTime dt2 = formatter.parseDateTime(linksList.get(1).getCreatedDate().toString());
//
//        System.err.println(dt);
//        System.err.println("dt.isBeforeNow() "+dt.isBeforeNow());
//        System.err.println("dt.isBefore(dt2) "+dt.isBefore(dt2));

        System.err.println(linksList);

        linksList.sort(new Comparator<Links>() {
            @Override
            public int compare(Links o1, Links o2) {

                //Mon Aug 23 13:57:18 IRDT 2021
                DateTimeFormatter formatter = DateTimeFormat.forPattern("E M d HH:mm:ss Z Y");
                DateTime dt = formatter.parseDateTime(o1.getCreatedDate().toString());
                DateTime dt2 = formatter.parseDateTime(o2.getCreatedDate().toString());

                System.err.println(dt);
                System.err.println("dt.isBeforeNow() " + dt.isBeforeNow());
                System.err.println("dt.isBefore(dt2) " + dt.isBefore(dt2));


                return o1.getExpiresDate().compareTo(o2.getExpiresDate()) > 0 ? 1 : 0;
            }
        });

        System.err.println(linksList);
    }


}
