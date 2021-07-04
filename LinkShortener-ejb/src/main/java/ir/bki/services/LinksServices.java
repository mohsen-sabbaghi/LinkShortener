package ir.bki.services;

import ir.bki.dao.LinksDao;
import ir.bki.dto.LinkDto;
import ir.bki.entities.Links;
import ir.bki.producers.HashThisProducer;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;


@Stateless
@Getter
//@Singleton
public class LinksServices {

    @Inject
    public LinksDao linksDao;

    @Inject
    public HashThisProducer hashids;


    public Links createLink(LinkDto linkDto) {
        Links links = new Links();
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
        DateTime dateTime = parser.parseDateTime(linkDto.getExpiresDate());
        links.setRedirectLink(linkDto.getLongLink());
        links.setExpiresDate(dateTime.toDate());
        links.setHttpStatusCode(201);
        links.setEnabled(true);
        Links response = linksDao.create(links);
//        String encodedLink = baseConversion.encode(response.getId());
        String encodedLink = hashids.getHashThisInstance().encode(response.getId());
        if (linkDto.getDesiredlink() != null) {
            encodedLink = linkDto.getDesiredlink();
        }
        links.setShortLink(encodedLink);
        return linksDao.getEm().merge(links);
    }

    public Links retrieveOne(long id) {
        Links res;
        try {
            res = linksDao.getEm().createNamedQuery(Links.FIND_BY_ID, Links.class).setParameter("id", id).getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
        return res;
    }

    public Links longUrlAlreadyExist(String longUrl) {
        Links res = null;
        try {
            res = (Links) linksDao.getEm()
                    .createNamedQuery(Links.FIND_BY_LONG_URL)
                    .setParameter("redirectLink", longUrl).getSingleResult();
        } catch (Exception ignored) {
        }
        return res;
    }

    public Links shortUrlAlreadyExist(String shortUrl) {
        Links res = null;
        try {
            res = (Links) linksDao.getEm()
                    .createNamedQuery(Links.FIND_BY_SHORT_URL)
                    .setParameter("shortLink", shortUrl).getSingleResult();
        } catch (Exception ignored) {
        }
        return res;
    }

    public boolean isExpired(Links links) {
        DateTime second = new DateTime(links.getExpiresDate());
        return second.isBeforeNow();
    }
}
