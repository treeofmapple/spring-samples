export interface ASO {
  id: string;
  employee: string;
  company: string;
  examDate: string;
  examType: 'admission' | 'periodic' | 'return_to_work' | 'dismissal';
  doctor: string;
  result: 'fit' | 'unfit' | 'fit_with_restrictions';
  issuanceDate: string;
  validUntil?: string;
  asoNumber: string;
  status: 'issued' | 'pending' | 'cancelled';
  observations?: string;
  restrictions?: string;
}