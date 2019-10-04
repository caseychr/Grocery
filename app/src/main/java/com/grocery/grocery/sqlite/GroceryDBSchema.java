package com.grocery.grocery.sqlite;

public class GroceryDBSchema {

    public static final class GroceryTable{
        public static final String NAME = "groceries";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String GROCERY_NAME = "grocery_name";
            public static final String GROCERY_AMOUNT = "grocery_amount";
            public static final String GROCERY_LOCATION = "grocery_location";
            public static final String GROCERY_COMPLETED = "grocery_completed";
            public static final String GROCERY_RECURRING = "grocery_recurring";
        }
    }
}
