CREATE DATABASE familyhealth;
USE familyhealth;

CREATE TABLE roles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL
); 

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL DEFAULT '',
    fullname VARCHAR(100) DEFAULT '',
    gender ENUM('MALE','FEMALE','OTHER') NOT NULL,
    date_of_birth DATE, 
    email VARCHAR(100) DEFAULT '',
    cccd VARCHAR(12) DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active TINYINT(1) DEFAULT 1,
    profile_id INT,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE households(
    id INT PRIMARY KEY AUTO_INCREMENT,
    address VARCHAR(200) DEFAULT '',
    quantity INT DEFAULT 1,
    is_active TINYINT(1) DEFAULT 1
); 

CREATE TABLE members(
    id INT PRIMARY KEY AUTO_INCREMENT,
    relation ENUM('CHU_HO','VO','CHONG','CON') DEFAULT 'CHU_HO',
    bhyt VARCHAR(12) DEFAULT '',
    household_id INT,
    user_id INT,
    FOREIGN KEY (household_id) REFERENCES households(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
); 


CREATE TABLE doctors(
    id INT PRIMARY KEY AUTO_INCREMENT,
    expertise ENUM('TIM_MACH','HO_HAP','TIEU_HOA','THAN_KINH','TAI_MUI_HONG','MAT','DA_LIEU','PHU_KHOA') DEFAULT '',
    bio VARCHAR(255) DEFAULT '',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
); 

CREATE TABLE appointments(
    id INT PRIMARY KEY AUTO_INCREMENT,
    time DATE, 
    status ENUM('SCHEDULED','COMPLETED','CANCELLED') NOT NULL,
    note VARCHAR(200) DEFAULT '',
    doctor_id INT,
    member_id INT,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
); 

CREATE TABLE medical_result(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    note VARCHAR(200) NOT NULL,
    total_money FLOAT NOT NULL, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    appointment_id INT,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
); 

CREATE TABLE payments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    payment_method ENUM('VNPAY', 'MOMO', 'ZALOPAY') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    transaction_code VARCHAR(100) UNIQUE,
    paid_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medical_result_id) REFERENCES medical_result(id)
);
