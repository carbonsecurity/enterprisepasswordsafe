/*
 * Copyright (c) 2017 Carbon Security Ltd. <opensource@carbonsecurity.co.uk>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.enterprisepasswordsafe.engine.database;

import com.enterprisepasswordsafe.engine.configuration.JDBCConfiguration;
import com.enterprisepasswordsafe.engine.tests.utils.PasswordTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test cases for the PasswordDAO
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JDBCConfiguration.class)
@PowerMockIgnore("javax.management.*")
public class PasswordDAOTests extends EmbeddedDatabaseTestBase {

    @Test
    public void testStoreNewPassword()
            throws GeneralSecurityException, IOException, SQLException {
        String runId = Long.toString(System.currentTimeMillis());
        String passwordId = PasswordTestUtils.createPassword(runId, getAdminUser());

        User adminUser = getAdminUser();
        Password retrieved = PasswordDAO.getInstance().getById(adminUser, passwordId);
        assertThat(retrieved.getUsername(), is("u" + runId));
        assertThat(retrieved.getPassword(), is("p" + runId));
        assertThat(retrieved.getLocation(), is("l" + runId));
        assertThat(retrieved.getNotes(),    is("n" + runId));
    }

    @Test
    public void testPerformRawAPISearch()
            throws GeneralSecurityException, IOException, SQLException {
        String runId = Long.toString(System.currentTimeMillis());
        User adminUser = getAdminUser();
        String passwordId = PasswordTestUtils.createPassword(runId, adminUser);
        Set<String> ids = PasswordDAO.getInstance().performRawAPISearch(adminUser, "u" + runId, "l" + runId);
        assertThat(ids.isEmpty(), is(false));
        assertThat(ids.contains(passwordId), is(true));
    }

    @Test
    public void testGetPasswordsRestrictionAppliesTo()
            throws SQLException, GeneralSecurityException, IOException {
        String runId = Long.toString(System.currentTimeMillis());
        String passwordId = PasswordTestUtils.createPassword(runId, getAdminUser());

        PasswordRestrictionDAO prDAO = PasswordRestrictionDAO.getInstance();
        PasswordRestriction createdRestriction = new PasswordRestriction("pr_"+runId, 0, 0, 0, 0, 0, 0, "", 0);
        prDAO.store(createdRestriction);

        PasswordDAO pDAO = PasswordDAO.getInstance();
        User adminUser = getAdminUser();
        Password password = pDAO.getById(adminUser, passwordId);
        password.setRestrictionId(createdRestriction.getId());
        pDAO.update(password, adminUser);

        List<Password> passwords
            = pDAO.getPasswordsRestrictionAppliesTo(adminUser, createdRestriction.getId());
        assertThat(passwords.isEmpty(), is(false));

        boolean found = false;
        for(Password thisPassword: passwords) {
            if(thisPassword.getId().equals(passwordId)) {
                found = true;
                break;
            }
        }
        assertThat(found, is(true));
    }
}
