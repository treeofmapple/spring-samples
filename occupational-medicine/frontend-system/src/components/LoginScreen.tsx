import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Alert, AlertDescription } from './ui/alert';
import { useAuth } from '../App';
import pt from '../i18n/pt-BR';

export function LoginScreen() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { user, login } = useAuth();

  if (user) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    
    const success = login(email, password);
    if (!success) {
      setError('Invalid email or password. Try: admin@clinic.com / doctor@clinic.com / employee@clinic.com with password: password');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100 p-4">
      <div className="w-full max-w-md">
        <Card className="shadow-lg">
          <CardHeader className="space-y-4 text-center">
            <div className="mx-auto w-20 h-20 bg-blue-600 rounded-full flex items-center justify-center">
              <svg className="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
              </svg>
            </div>
            <CardTitle className="text-2xl">{pt['login.title']}</CardTitle>
            <CardDescription>
              {pt['login.description']}
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">{pt['login.email']}</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder={pt['login.placeholderEmail']}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">{pt['login.password']}</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder={pt['login.placeholderPassword']}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              {error && (
                <Alert variant="destructive">
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
              <Button type="submit" className="w-full bg-blue-600 hover:bg-blue-700">
                {pt['login.signIn']}
              </Button>
            </form>
            <div className="mt-6 p-4 bg-gray-50 rounded-md">
              <p className="text-sm text-gray-600 mb-2">{pt['login.demoAccounts']}</p>
              <div className="text-xs space-y-1">
                <p><strong>{pt['login.demo.admin'].split(':')[0]}:</strong> admin@clinic.com</p>
                <p><strong>{pt['login.demo.doctor'].split(':')[0]}:</strong> doctor@clinic.com</p>
                <p><strong>{pt['login.demo.employee'].split(':')[0]}:</strong> employee@clinic.com</p>
                <p><strong>{pt['login.demo.password'].split(':')[0]}:</strong> password</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}