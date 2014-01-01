package com.adaptavant.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
  private static final PersistenceManagerFactory pmfInstance =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  private PMF() {}

  public static PersistenceManagerFactory getPMF() {
    return pmfInstance;
  }
}
