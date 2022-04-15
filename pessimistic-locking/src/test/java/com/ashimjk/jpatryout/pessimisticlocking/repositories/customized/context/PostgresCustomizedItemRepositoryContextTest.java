package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("derby")
class PostgresCustomizedItemRepositoryContextTest extends AbstractCustomizedItemRepositoryContextTest {}