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
}

export interface UserPlant extends Plant{
    nickname: string | null;
    last_watered: number | null;
    user_plant_id: number | null;
}

export interface CareGuide {
    type: string;
    description: string;
}
