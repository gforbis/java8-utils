/*
 * Copyright (c) 2019 BTS-IT, Inc. All Rights Reserved.
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright Office.
 */
package gwf.utils.hibernate;

import java.util.Collections;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.Session;

public class HibernateUtils {
    public static String getNativeQuery(javax.persistence.Query query, Session session) {
        String hql = query.unwrap(org.hibernate.query.Query.class).getQueryString();
        ASTQueryTranslatorFactory factory = new ASTQueryTranslatorFactory();
        SessionImplementor hibSession = session.unwrap(SessionImplementor.class);
        QueryTranslator trans = factory.createQueryTranslator("", hql, Collections.EMPTY_MAP, hibSession.getFactory(), null);
        trans.compile(Collections.EMPTY_MAP, false);
        return trans.getSQLString();
    }
}
