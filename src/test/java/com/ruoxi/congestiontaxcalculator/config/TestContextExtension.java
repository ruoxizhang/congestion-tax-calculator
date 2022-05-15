package com.ruoxi.congestiontaxcalculator.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestContextExtension implements BeforeAllCallback {

  private static final ExtensionContext.Namespace NAMESPACE =
      ExtensionContext.Namespace.create(TestContextExtension.class);
  private static final String CONTEXT = TestContext.class.getName();

  @Override
  public void beforeAll(ExtensionContext context) {
    // Start dependencies and save in the root store
    ExtensionContext.Store store = context.getRoot().getStore(NAMESPACE);
    store.getOrComputeIfAbsent(CONTEXT, TestContext::build, TestContext.class);
  }
}
