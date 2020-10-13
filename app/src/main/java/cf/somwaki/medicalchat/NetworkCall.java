package cf.somwaki.medicalchat;


class NetworkCall {
    static String jsonData = "{\n" +
            "    \"choice\": \"1\",\n" +
            "    \"age\": \"35\",\n" +
            "    \"next_question\": {\n" +
            "        \"extras\": {},\n" +
            "        \"items\": [\n" +
            "            {\n" +
            "                \"choices\": [\n" +
            "                    {\n" +
            "                        \"id\": \"present\",\n" +
            "                        \"label\": \"Yes\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"id\": \"absent\",\n" +
            "                        \"label\": \"No\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"id\": \"unknown\",\n" +
            "                        \"label\": \"Don't know\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"id\": \"s_1535\",\n" +
            "                \"name\": \"Headache, chronic\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"text\": \"Have you been experiencing these headaches for more than three months?\",\n" +
            "        \"type\": \"single\"\n" +
            "    },\n" +
            "    \"sex\": \"male\",\n" +
            "    \"symptoms\": [\n" +
            "        {\n" +
            "            \"choice_id\": \"present\",\n" +
            "            \"id\": \"s_21\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"choice_id\": \"present\",\n" +
            "            \"id\": \"s_98\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"choice_id\": \"absent\",\n" +
            "            \"id\": \"s_107\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"choice_id\": \"present\",\n" +
            "            \"id\": \"s_1193\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"choice_id\": \"absent\",\n" +
            "            \"id\": \"s_100\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";


    public static String myJsonString = "{\n" +
            "    \"mentions\": [\n" +
            "        {\n" +
            "            \"id\": \"s_370\",\n" +
            "            \"name\": \"Dizziness\",\n" +
            "            \"common_name\": \"Dizzy\",\n" +
            "            \"orth\": \"feeling dizzy\",\n" +
            "            \"choice_id\": \"present\",\n" +
            "            \"type\": \"symptom\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"obvious\": false\n" +
            "}";
}
