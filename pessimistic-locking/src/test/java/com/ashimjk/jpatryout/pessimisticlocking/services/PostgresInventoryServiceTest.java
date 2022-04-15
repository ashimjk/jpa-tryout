package com.ashimjk.jpatryout.pessimisticlocking.services;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("postgres")
class PostgresInventoryServiceTest extends AbstractInventoryServiceTest {}