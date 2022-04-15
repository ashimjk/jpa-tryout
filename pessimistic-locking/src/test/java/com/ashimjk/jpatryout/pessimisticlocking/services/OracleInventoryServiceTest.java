package com.ashimjk.jpatryout.pessimisticlocking.services;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-oracle")
class OracleInventoryServiceTest extends InventoryServiceTest {}