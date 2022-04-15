package com.ashimjk.jpatryout.pessimisticlocking.services;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-postgres")
class PostgresInventoryServiceTest extends InventoryServiceTest {}