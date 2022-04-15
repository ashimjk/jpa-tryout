package com.ashimjk.jpatryout.pessimisticlocking.services;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-derby")
class DerbyInventoryServiceTest extends InventoryServiceTest {}