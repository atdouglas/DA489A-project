export interface Plant {
    id: number;
    common_name: string | null;
    scientific_name: string | null;
    family: string | null;
    image_url: string | null;
    maintenance: string | null;
    light: string | null;
    description: string | null;
    watering_frequency: number | null;
    poisonous_to_pets: boolean | null;
    nickname?: string;
}

export interface UserPlant extends Plant{

}
