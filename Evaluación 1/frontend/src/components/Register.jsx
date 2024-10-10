import React, { useState } from 'react';
import '../styles/Register.css';

function Register() {
  const [form, setForm] = useState({
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    rut: '',
    edad: 18, // valor inicial 18
    salario: ''
  });

  const [errors, setErrors] = useState({
    rut: '',
    edad: '',
    salario: ''
  });

  // Validar formato del RUT
  const validateRut = (rut) => {
    const rutRegex = /^[0-9]{1,2}\.[0-9]{3}\.[0-9]{3}-[0-9Kk]{1}$/; // Formato 12.345.678-9
    return rutRegex.test(rut);
  };

  // Validar salario solo números
  const validateSalario = (salario) => {
    const salarioRegex = /^[0-9]+$/; // Solo acepta números
    return salarioRegex.test(salario);
  };

  // Manejar cambios en los campos
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });

    // Validaciones dinámicas
    if (name === 'rut' && !validateRut(value)) {
      setErrors({ ...errors, rut: 'El RUT debe tener el formato 12.345.678-9' });
    } else if (name === 'rut') {
      setErrors({ ...errors, rut: '' });
    }

    if (name === 'salario' && !validateSalario(value)) {
      setErrors({ ...errors, salario: 'El salario solo puede contener números' });
    } else if (name === 'salario') {
      setErrors({ ...errors, salario: '' });
    }
  };

  // Manejar envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validaciones finales antes de enviar
    if (!validateRut(form.rut)) {
      alert('Por favor ingresa un RUT válido.');
      return;
    }

    if (!validateSalario(form.salario)) {
      alert('El salario debe contener solo números.');
      return;
    }

    const customerData = {
      name: form.nombre,
      lastName: form.apellido,
      email: form.email,
      password: form.password,
      rut: form.rut,
      age: form.edad,
      salary: form.salario
    };

    try {
      const response = await fetch('http://192.168.1.10:8090/customer/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(customerData)
      });

      if (response.ok) {
        alert('Registro exitoso!');
        setForm({
          nombre: '',
          apellido: '',
          email: '',
          password: '',
          rut: '',
          edad: 18,
          salario: ''
        });
      } else {
        const errorMessage = await response.text();
        alert('Error: ' + errorMessage);
      }
    } catch (error) {
      alert('Error al registrar el cliente: ' + error.message);
    }
  };

  return (
    <div className="register-container">
      <h2>Registro de Usuario</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Nombre:</label>
          <input
            type="text"
            name="nombre"
            value={form.nombre}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Apellido:</label>
          <input
            type="text"
            name="apellido"
            value={form.apellido}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Contraseña:</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>RUT:</label>
          <input
            type="text"
            name="rut"
            value={form.rut}
            onChange={handleChange}
            required
          />
          {errors.rut && <p className="error">{errors.rut}</p>}
        </div>

        <div>
          <label>Edad:</label>
          <select name="edad" value={form.edad} onChange={handleChange} required>
            {[...Array(43)].map((_, index) => {
              const edad = index + 18;
              return (
                <option key={edad} value={edad}>
                  {edad}
                </option>
              );
            })}
          </select>
        </div>

        <div>
          <label>Salario:</label>
          <input
            type="text"
            name="salario"
            value={form.salario}
            onChange={handleChange}
            required
          />
          {errors.salario && <p className="error">{errors.salario}</p>}
        </div>

        <button type="submit" className='btn-register'>Registrarse</button>
      </form>
    </div>
  );
}

export default Register;
