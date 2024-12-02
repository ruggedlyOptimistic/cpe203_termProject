class EntityParser
{
    boolean parseObstacle(String[] properties, WorldModel world, ImageStore imageStore)
    {
        final String OBSTACLE_KEY = "obstacle";
        final int OBSTACLE_NUM_PROPERTIES = 4;
        final int OBSTACLE_ID = 1;
        final int OBSTACLE_COL = 2;
        final int OBSTACLE_ROW = 3;

        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]), Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity obstacle = new Obstacle(properties[OBSTACLE_ID], pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(obstacle);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    boolean parseAtlantis(String[] properties, WorldModel world, ImageStore imageStore)
    {
        final String ATLANTIS_KEY = "atlantis";
        final int ATLANTIS_NUM_PROPERTIES = 4;
        final int ATLANTIS_ID = 1;
        final int ATLANTIS_COL = 2;
        final int ATLANTIS_ROW = 3;
        final int ATLANTIS_ANIMATION_PERIOD = 70;

        if (properties.length == ATLANTIS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]), Integer.parseInt(properties[ATLANTIS_ROW]));
            Atlantis atlantis = new Atlantis(properties[ATLANTIS_ID], pt, imageStore.getImageList(ATLANTIS_KEY),0, ATLANTIS_ANIMATION_PERIOD,0);
            world.tryAddEntity(atlantis);
        }  // Declared atlantis as Atlantis rather than Entity

        return properties.length == ATLANTIS_NUM_PROPERTIES;
    }

    boolean parseFish(String[] properties, WorldModel world, ImageStore imageStore)
    {
        final String FISH_KEY = "fish";
        final int FISH_NUM_PROPERTIES = 5;
        final int FISH_ID = 1;
        final int FISH_COL = 2;
        final int FISH_ROW = 3;
        final int FISH_ACTION_PERIOD = 4;

        if (properties.length == FISH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[FISH_COL]), Integer.parseInt(properties[FISH_ROW]));
            Entity fish = new Fish(properties[FISH_ID], pt,imageStore.getImageList(FISH_KEY), Integer.parseInt(properties[FISH_ACTION_PERIOD]));
            world.tryAddEntity(fish);
        }

        return properties.length == FISH_NUM_PROPERTIES;
    }

    boolean parseSgrass(String[] properties, WorldModel world, ImageStore imageStore)
    {
        final String SGRASS_KEY = "seaGrass";
        final int SGRASS_NUM_PROPERTIES = 5;
        final int SGRASS_ID = 1;
        final int SGRASS_COL = 2;
        final int SGRASS_ROW = 3;
        final int SGRASS_ACTION_PERIOD = 4;

        if (properties.length == SGRASS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                    Integer.parseInt(properties[SGRASS_ROW]));
            Entity sGrass = new SGrass(properties[SGRASS_ID], pt, imageStore.getImageList(SGRASS_KEY), Integer.parseInt(properties[SGRASS_ACTION_PERIOD]));
            world.tryAddEntity(sGrass);
        }

        return properties.length == SGRASS_NUM_PROPERTIES;
    }

    boolean parseOctoNotFull(String[] properties, WorldModel world, ImageStore imageStore)
    {
        final String OCTO_KEY = "octo";
        final int OCTO_NUM_PROPERTIES = 7;
        final int OCTO_ID = 1;
        final int OCTO_COL = 2;
        final int OCTO_ROW = 3;
        final int OCTO_LIMIT = 4;
        final int OCTO_ACTION_PERIOD = 5;
        final int OCTO_ANIMATION_PERIOD = 6;

        if (properties.length == OCTO_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[OCTO_COL]), Integer.parseInt(properties[OCTO_ROW]));
            OctoNotFull octo = new OctoNotFull(properties[OCTO_ID], pt, imageStore.getImageList(OCTO_KEY), Integer.parseInt(properties[OCTO_ACTION_PERIOD]), 0,
                    Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]), Integer.parseInt(properties[OCTO_LIMIT]), 0);
            world.tryAddEntity(octo);
        }

        return properties.length == OCTO_NUM_PROPERTIES;
    }
}
